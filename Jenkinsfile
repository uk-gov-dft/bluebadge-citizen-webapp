def build_number = "${env.BUILD_NUMBER}"
def REPONAME = "${scm.getUserRemoteConfigs()[0].getUrl()}"

node {

    stage('Clone sources') {
      git(
           url: "${REPONAME}",
           credentialsId: 'githubsshkey',
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
        }
        finally {
            junit '**/TEST*.xml'
        }
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

            timeout(time: 10, unit: 'MINUTES') {
                try {
                    sh 'echo $PATH && cd acceptance-tests && bash run-regression.sh'
                }
                finally {
                    archiveArtifacts allowEmptyArchive: true, artifacts: '**/docker.log'
                    junit '**/TEST*.xml'
                }
            }
        }
    }
}
