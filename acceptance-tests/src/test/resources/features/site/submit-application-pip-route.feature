@SubmitApplicationPIPRoute
Feature: DFT Blue badge Citizen app new application - PIP
  As a citizen user I want to be able to create a new application via PIP route

  Scenario: Create a successful new application for myself - England - 8 or more points
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "PIP"
    And   I complete moving around points page for "12"
    Then  I should see "You're" eligible page
    When  I complete eligible page

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
    And   I can click on the "Prove identity" link
    And   I complete "prove ID" page with a "GIF" document

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Prove address" link
    And   I complete "prove address" page with a "GIF" document

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Add a photo for the badge" link
    And   I complete "provide photo" page with a "GIF" document

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Agree to declaration" link
    And   I complete declaration page

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Submit application" link
    And   I complete Submit application page
    Then  I should see page titled "Application submitted" with GOV.UK suffix

  Scenario: Create a successful new application for someone else - England - 8 or more points
    Given I complete applicant page for "someone else"
    And   I skip find council page
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "NO"
    And   I complete receive benefit page for "PIP"
    And   I complete moving around points page for "12"
    Then  I should see "They're" eligible page


  Scenario: Create a successful new application for myself - England - less than 8 points
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES BUT DON'T KNOW"
    And   I complete receive benefit page for "PIP"
    And   I complete moving around points page for "4"
    And   I complete main reason page for "WALKD"
    And   I complete what makes walking difficult page for "HELP"
    Then  I should see "You may be" eligible page


  Scenario: Create a successful new application for someone else - England - than 8 points
    Given I complete applicant page for "someone else"
    And   I skip find council page
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES BUT DON'T KNOW"
    And   I complete receive benefit page for "PIP"
    And   I complete moving around points page for "4"
    And   I complete main reason page for "WALKD"
    And   I complete what makes walking difficult page for "HELP"
    Then  I should see "They may be" eligible page


  Scenario: Create a successful new application for myself - Scotland - less than 8 points (moving) - 12 points (planning)
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page for "scotland"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "PIP"
    And   I complete moving around points page for "4"
    And   I complete planning points page for "12"
    Then  I should see "You're" eligible page


  Scenario: Create a successful new application for someone else - Scotland - less than 8 points (moving) - 12 points (planning)
    Given I complete applicant page for "someone else"
    And   I skip find council page
    And   I complete select council page for "scotland"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "PIP"
    And   I complete moving around points page for "4"
    And   I complete planning points page for "12"
    Then  I should see "They're" eligible page


  Scenario: Create a successful new application for myself - Scotland - less than 8 points (moving) - less than 12 points (planning) - DLA Yes
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page for "scotland"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "PIP"
    And   I complete moving around points page for "4"
    And   I complete planning points page for "8"
    And   I complete dla allowance page for "YES"
    Then  I should see "You're" eligible page


  Scenario: Create a successful new application for someone else - Scotland - less than 8 points (moving) - less 12 points (planning)b - DLA Yes - (with validation messages)
    Given I complete applicant page for "someone else"
    And   I skip find council page
    And   I complete select council page for "scotland"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "PIP"
    And   I complete moving around points page for "4"
    And   I complete planning points page for "8"
    And   I complete dla allowance page for "YES"
    Then  I should see "They're" eligible page


  Scenario: Create a successful new application for myself - Scotland - less than 8 points (moving) - less than 12 points (planning) - DLA No
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page for "scotland"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "PIP"
    And   I complete moving around points page for "4"
    And   I complete planning points page for "8"
    And   I complete dla allowance page for "NO"
    And   I complete main reason page for "WALKD"
    And   I complete what makes walking difficult page for "HELP"
    Then  I should see "You may be" eligible page

  Scenario: Create a successful new application for someone else - Scotland - less than 8 points (moving) - less 12 points (planning) - DLA Yes
    Given I complete applicant page for "someone else"
    And   I skip find council page
    And   I complete select council page for "scotland"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "PIP"
    And   I complete moving around points page for "4"
    And   I complete planning points page for "8"
    And   I complete dla allowance page for "NO"
    And   I complete main reason page for "WALKD"
    And   I complete what makes walking difficult page for "HELP"
    Then  I should see "They may be" eligible page

  Scenario: Create a successful new application for myself - Wales - less than 8 points (moving) - 12 points (planning)
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page for "wales"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "PIP"
    And   I complete moving around points page for "4"
    And   I complete planning points page for "12"
    Then  I should see "You're" eligible page


  Scenario: Create a successful new application for someone else - Wales - less than 8 points (moving) - 12 points (planning)
    Given I complete applicant page for "someone else"
    And   I skip find council page
    And   I complete select council page for "wales"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "PIP"
    And   I complete moving around points page for "4"
    And   I complete planning points page for "12"
    Then  I should see "They're" eligible page


  Scenario: Create a successful new application for myself - Wales - less than 8 points (moving) - less than 12 points (planning)
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page for "wales"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "PIP"
    And   I complete moving around points page for "4"
    And   I complete planning points page for "8"
    And   I complete main reason page for "WALKD"
    And   I complete what makes walking difficult page for "HELP"
    Then  I should see "You may be" eligible page

  Scenario: Create a successful new application for someone else - Wales - less than 8 points (moving) - less than 12 points (planning)
    Given I complete applicant page for "someone else"
    And   I skip find council page
    And   I complete select council page for "wales"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "PIP"
    And   I complete moving around points page for "4"
    And   I complete planning points page for "8"
    And   I complete main reason page for "WALKD"
    And   I complete what makes walking difficult page for "HELP"
    Then  I should see "They may be" eligible page
