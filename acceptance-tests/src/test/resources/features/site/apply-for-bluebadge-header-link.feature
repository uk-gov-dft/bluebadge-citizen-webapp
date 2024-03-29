@ApplyForABlueBadgeHeaderLink
Feature: The 'Apply for a Blue Badge' link in the citizen app header should
         take you back to the start of the application

  As a citizen
  If I click  the Apply for a Blue Badge header
  I should see the Applicant page

  Scenario: Verify I see Applicant page when I click on Apply for a Blue Badge header link when I am in eligibility page
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "PIP"
    And   I complete moving around points page for "12"
    Then  I should see "You're" eligible page
    When  I click on "Apply for a Blue Badge" link
    Then  I should see the page titled "Who are you applying for?" with GOV.UK suffix
    And   I should see the "yourself" option button is selected in the Who are you applying for page


  Scenario: Verify I see Applicant page when I click on Apply for a Blue Badge header link when I am in address page
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "NO"
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
    Then  I should see the page titled "What's your address? - Apply for a Blue Badge - GOV.UK"
    When  I click on "Apply for a Blue Badge" link
    Then  I should see the page titled "Who are you applying for?" with GOV.UK suffix
    And   I should see the "yourself" option button is selected in the Who are you applying for page
