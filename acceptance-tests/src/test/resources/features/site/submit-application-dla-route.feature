@SubmitApplicationDLARoute
Feature: DFT Blue badge Citizen app new application - DLA
  As a citizen user I want to be able to create a new application via DLA route

  # FOR MYSELF
  Scenario: Create a successful new application for myself via DLA route - Yes
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "DLA"
    And   I complete has mobility component page for "YES"
    Then  I should see "You're" eligible page
    When  I complete eligible page

    Then  I see the "DLA" task list page
    And   I can click on the "Enter personal details" link
    And   I complete what's your name page
    And   I complete date of birth page for "ADULT"
    And   I complete gender page for "Man"
    And   I complete NI number page
    And   I complete address page
    And   I complete contact page for "yourself"

    Then  I see the "DLA" task list page as Adult
    And   I see task "Enter personal details" as COMPLETED
    And   I can click on the "Provide proof of benefit" link
    And   I complete prove benefit page for "no"
    And   I complete "upload benefit" page with no documents

    Then  I see the "DLA" task list page as Adult
    And   I can click on the "Prove identity" link
    And   I complete "prove ID" page with no documents

    Then  I see the "DLA" task list page as Adult
    And   I can click on the "Prove address" link
    And   I complete "prove address" page with a "JPG" document

    Then  I see the "DLA" task list page as Adult
    And   I can click on the "Add a photo of yourself" link
    And   I complete "provide photo" page with no documents

    Then  I see the "DLA" task list page as Adult
    And   I can click on the "Read Declaration" link
    And   I complete declaration page

    Then  I see the "DLA" task list page as Adult
    And   I can click on the "Submit application" link
    And   I complete Submit application page
    Then  I should see the page titled "Application submitted" with GOV.UK suffix


  Scenario: Create a successful new application for myself via DLA route - No
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "NO"
    And   I complete receive benefit page for "DLA"
    And   I complete has mobility component page for "NO"
    And   I complete main reason page for "WALKD"
    And   I complete what makes walking difficult page for "HELP"
    Then  I should see "You may be" eligible page

  # FOR SOMEONE ELSE
  Scenario: Create a successful new application for someone else - DLA rout - Yes
    Given I complete applicant page for "someone else"
    And   I skip find council page
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "DLA"
    And   I complete has mobility component page for "YES"
    Then  I should see "They're" eligible page

  Scenario: Create a successful new application for someone else - DLA route - No
    Given I complete applicant page for "someone else"
    And   I skip find council page
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES BUT DON'T KNOW"
    And   I complete receive benefit page for "DLA"
    And   I complete has mobility component page for "NO"
    And   I complete main reason page for "WALKD"
    And   I complete what makes walking difficult page for "HELP"
    Then  I should see "They may be" eligible page
