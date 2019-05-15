@SaveApplication
Feature: DFT Blue badge Citizen app - Save application
  As a citizen user I want to be able to save application and retrieve it later.

  Scenario: Save application and retrieve
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
    Then  I see the "PIP" task list page

    And   I can click on the "Save and return later" link
    Then  I should see page titled "Save your progress" with GOV.UK suffix
    And   I validate save application page for a "yourself" application

    Then  I see the "PIP" task list page
    And   I navigate to the "return-to-application" page
    Then  I should see page titled "Return to a saved application" with GOV.UK suffix
    And   I validate return to saved application page for a "yourself" application
    
    Then  I should see page titled "Enter the 4-digit code" with GOV.UK suffix
    And   I validate enter the 4 digit code page for a "yourself" application







