def build_number = "${env.BUILD_NUMBER}"
def REPONAME = "${scm.getUserRemoteConfigs()[0].getUrl()}"

node {

    stage('Clone sources') {
      cleanWs()
      git(
           url: "${REPONAME}",
           credentialsId: 'username***REMOVED***-github-automation-uk-gov-dft',
           branch: "${BRANCH_NAME}"
        )
     }

    stage('Read Version') {
      def version = readFile('VERSION').trim()
      println "${version}"
    }

    stage ('Gradle build') {
        try {
            sh 'echo $(whoami)'
            sh 'bash -c "source /etc/profile && (npm list gulp@3.9.1 -g || npm install -g gulp@3.9.1) && npm install && npm run prod"'
            sh './gradlew --no-daemon --profile --configure-on-demand clean build bootJar artifactoryPublish artifactoryDeploy'
            sh 'mv build/reports/profile/profile-*.html build/reports/profile/index.html'
            sh 'ls -la build/libs'
            stash includes: 'build/**/*', name: 'build'
        }
        finally {
            junit '**/TEST*.xml'
        }
    }

    stage ('DockerPublish') {
      node('Functional') {
        git(
           url: "${REPONAME}",
           credentialsId: 'dft-buildbot-valtech',
           branch: "${BRANCH_NAME}"
        )

        unstash 'build'

        sh 'ls -la'

        withCredentials([string(credentialsId: 'GITHUB_TOKEN', variable: 'GITHUB_TOKEN')]) {
          sh '''
            ls -la build/libs
            curl -s -o docker-publish.sh -H "Authorization: token ${GITHUB_TOKEN}" -H 'Accept: application/vnd.github.v3.raw' -O -L https://raw.githubusercontent.com/uk-gov-dft/shell-scripts/master/docker-publish.sh
            ls -la
            bash docker-publish.sh
          '''
        }
      }
    }

    stage ('OWASP Dependency Check') {
        sh './gradlew dependencyCheckUpdate dependencyCheckAggregate'

        publishHTML (target: [
         allowMissing: false,
         alwaysLinkToLastBuild: false,
         keepAll: true,
         reportDir: 'build/reports',
         reportFiles: 'dependency-check-report.html',
         reportName: "OWASP Dependency Check"
        ])
    }

    stage('SonarQube analysis') {
        withSonarQubeEnv('sonarqube') {
            def ver = readFile('VERSION').trim()
            echo "Version: " + ver
            // requires SonarQube Scanner for Gradle 2.1+
            // It's important to add --info because of SONARJNKNS-281
            sh "./gradlew --info sonarqube -Dsonar.projectName=citizen-webapp -Dsonar.projectVersion=${ver} -Dsonar.branch=${BRANCH_NAME}"
        }
    }

    stage("Quality Gate") {
        timeout(time: 5, unit: 'MINUTES') {
            def qg = waitForQualityGate()
            if (qg.status != 'OK') {
                error "Pipeline aborted due to quality gate failure: ${qg.status}"
            }
        }
    }

    stage("Acceptance Tests") {
        node('Functional') {
            git(
               url: "${REPONAME}",
               credentialsId: 'dft-buildbot-valtech',
               branch: "${BRANCH_NAME}"
            )

            timeout(time: 20, unit: 'MINUTES') {
                withEnv(["BASE_SELENIUM_URL=http://citizen-webapp:8780", "BASE_MANAGEMENT_URL=http://citizen-webapp:8781"]) {
                  withCredentials([string(credentialsId: 'GITHUB_TOKEN', variable: 'GITHUB_TOKEN')]) {
                    try {
                        sh '''
                            curl -s -o wait_for_it.sh -H "Authorization: token ${GITHUB_TOKEN}" -H 'Accept: application/vnd.github.v3.raw' -O -L https://raw.githubusercontent.com/uk-gov-dft/shell-scripts/master/wait_for_it.sh
                            docker pull elgalu/selenium
                            docker pull dosel/zalenium
                            docker run -d --network "dev-env-develop_default" --rm --name zalenium -p 4444:4444 -v /var/run/docker.sock:/var/run/docker.sock --privileged dosel/zalenium start --desiredContainers 4 --maxTestSessions 1 --keepOnlyFailedTests true --videoRecordingEnabled false --maxDockerSeleniumContainers 4
                            chmod +x ./wait_for_it.sh
                            ./wait_for_it.sh localhost:4444

                            echo " Base Selenium URL is $BASE_SELENIUM_URL"
                            echo " Base Management URL is $BASE_MANAGEMENT_URL"
                            cd acceptance-tests
                            curl -s -o run-regression-script.sh -H "Authorization: token ${GITHUB_TOKEN}" -H 'Accept: application/vnd.github.v3.raw' -O -L https://raw.githubusercontent.com/uk-gov-dft/shell-scripts/master/run-regression.sh
                            chmod +x run-regression-script.sh
                            ./run-regression-script.sh
                        '''
                    }
                    finally {
                        sh '''
                            docker kill zalenium || :
                            docker rm -vf zalenium || :
                        '''
                        archiveArtifacts allowEmptyArchive: true, artifacts: '**/docker.log'
                        junit '**/TEST*.xml'
                    }
                  }
                }
            }
        }
    }



}
