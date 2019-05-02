@TaskList
Feature: DFT Blue badge Citizen app - Task List
  As a citizen user I want to be able to create a new application using the functionality of the task list.

  Scenario: Create an application - complete tasks in any order
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "PIP"
    And   I complete moving around points page for "12"
    Then  I should see "You're" eligible page
    When  I complete eligible page

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Add a photo for the badge" link
    And   I complete "provide photo" page with a "GIF" document

    Then  I see the "PIP" task list page
    And   I can click on the "Enter personal details" link
    And   I complete what's your name page
    And   I complete date of birth page for "ADULT"
    And   I complete gender page for "Man"
    And   I complete NI number page
    And   I complete address page
    And   I complete contact page for "yourself"

    Then  I see the "PIP" task list page as Adult
    And   I see task "Enter personal details" as COMPLETED
    And   I can click on the "Provide proof of benefit" link
    And   I complete prove benefit page for "yes"
    And   I complete "upload benefit" page with a "GIF" document

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Prove address" link
    And   I complete "prove address" page with a "GIF" document

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Prove identity" link
    And   I complete "prove ID" page with a "GIF" document

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Agree to declaration" link
    And   I complete declaration page

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Submit application" link
    And   I complete Submit application page
    Then  I should see page titled "Application submitted" with GOV.UK suffix

  Scenario: Task list status - task status changes as each is complete
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "PIP"
    And   I complete moving around points page for "12"
    Then  I should see "You're" eligible page
    When  I complete eligible page

    Then  I see the "PIP" task list page as Adult
    And   I see task "Enter personal details" as NOT_STARTED
    And   I see task "Provide proof of benefit" as NOT_STARTED
    And   I see task "Prove identity" as NOT_STARTED
    And   I see task "Prove address" as NOT_STARTED
    And   I see task "Add a photo for the badge" as NOT_STARTED
    And   I see disabled task "Agree to declaration" as NOT_STARTED
    And   I see disabled task "Submit application" as NOT_STARTED

    And   I can click on the "Add a photo for the badge" link
    And   I complete "provide photo" page with no documents

    Then  I see the "PIP" task list page
    And   I see task "Enter personal details" as NOT_STARTED
    And   I see task "Provide proof of benefit" as NOT_STARTED
    And   I see task "Prove identity" as NOT_STARTED
    And   I see task "Prove address" as NOT_STARTED
    And   I see task "Add a photo for the badge" as COMPLETED
    And   I see disabled task "Agree to declaration" as NOT_STARTED

    And   I can click on the "Enter personal details" link
    And   I complete what's your name page
    And   I complete date of birth page for "ADULT"
    And   I complete gender page for "Man"
    And   I navigate to the "task-list" page
    Then  I see the "PIP" task list page
    And   I see task "Enter personal details" as IN_PROGRESS

    And   I can click on the "Enter personal details" link
    And   I complete what's your name page
    And   I complete date of birth page for "ADULT"
    And   I complete gender page for "Man"
    And   I complete NI number page
    And   I complete address page
    And   I complete contact page for "yourself"

    Then  I see the "PIP" task list page as Adult
    And   I see task "Enter personal details" as COMPLETED
    And   I see task "Provide proof of benefit" as NOT_STARTED
    And   I see task "Prove identity" as NOT_STARTED
    And   I see task "Prove address" as NOT_STARTED
    And   I see task "Add a photo for the badge" as COMPLETED
    And   I see disabled task "Agree to declaration" as NOT_STARTED

    And   I can click on the "Provide proof of benefit" link
    And   I complete prove benefit page for "yes"
    And   I complete "upload benefit" page with a "GIF" document

    Then  I see the "PIP" task list page as Adult
    And   I see task "Enter personal details" as COMPLETED
    And   I see task "Provide proof of benefit" as COMPLETED
    And   I see task "Prove identity" as NOT_STARTED
    And   I see task "Prove address" as NOT_STARTED
    And   I see task "Add a photo for the badge" as COMPLETED
    And   I see disabled task "Agree to declaration" as NOT_STARTED

    And   I can click on the "Prove address" link
    And   I complete "prove address" page with a "GIF" document

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Prove identity" link
    And   I complete "prove ID" page with a "GIF" document

    Then  I see the "PIP" task list page as Adult
    And   I see task "Enter personal details" as COMPLETED
    And   I see task "Provide proof of benefit" as COMPLETED
    And   I see task "Prove identity" as COMPLETED
    And   I see task "Prove address" as COMPLETED
    And   I see task "Add a photo for the badge" as COMPLETED
    And   I see task "Agree to declaration" as NOT_STARTED
    And   I see disabled task "Submit application" as NOT_STARTED

    And   I can click on the "Agree to declaration" link
    And   I complete declaration page
    And   I see task "Agree to declaration" as COMPLETED
    And   I see task "Submit application" as NOT_STARTED

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Submit application" link

  Scenario: Task list - Redirects to task list for invalid page
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "PIP"
    And   I complete moving around points page for "12"
    Then  I should see "You're" eligible page
    When  I complete eligible page
    Then  I see the "PIP" task list page as Adult
    And   I navigate to the "declaration" page
    Then  I see the "PIP" task list page as Adult
    And   I navigate to the "what-makes-walking-difficult" page
    Then  I see the "PIP" task list page as Adult

