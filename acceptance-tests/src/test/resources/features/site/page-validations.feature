@PageValidations
Feature: DFT Blue badge Citizen app new application - Page Validations via Walking Route
  As a citizen user I want to be able to see proper page titles and validation messages

  Scenario: Page title and error message validation - yourself
    Given I navigate to applicant page and validate for "yourself"
    And   I validate choose council page for "yourself" and select a council in "england"
    And   I validate local authority page for "yourself" in "england"
    And   I validate already have a blue badge page for "yourself" for "Yes"
    And   I validate benefit page for "yourself" for "NONE"
    And   I validate main reason page for "yourself" for "WALKD"
    And   I validate walking difficulty page for "yourself" for "HELP"
    Then  I should see "You may be" eligible page
    When  I complete eligible page
    And   I validate name page for a "yourself" application
    And   I validate date of birth page for a "yourself" application
    And   I validate gender page for a "yourself" application with option as "FEMALE"
    And   I validate nino page for a "yourself" application
    And   I validate enter address page for a "yourself" application
    And   I validate contact details page for a "yourself" application
    And   I validate health conditions page for a "yourself" application
    And   I validate what makes walking difficult page for a "yourself" application for "PAIN"

  Scenario: Page title and error message validation - someone else
    Given I navigate to applicant page and validate for "someone else"
    And   I validate choose council page for "someone else" and select a council in "wales"
    And   I validate local authority page for "someone else" in "wales"
    And   I validate already have a blue badge page for "someone else" for "No"
    And   I validate benefit page for "someone else" for "NONE"
    And   I validate main reason page for "someone else" for "WALKD"
    And   I validate walking difficulty page for "someone else" for "HELP"
    Then  I should see "They may be" eligible page
    When  I complete eligible page
    And   I validate name page for a "someone else" application
    And   I validate date of birth page for a "someone else" application
    And   I validate gender page for a "someone else" application with option as "MALE"
    And   I validate nino page for a "someone else" application
    And   I validate enter address page for a "someone else" application
    And   I validate health conditions page for a "someone else" application
    And   I validate what makes walking difficult page for a "someone else" application for "BALANCE"

  Scenario: Page title and error message validation - an organisation
    Given I navigate to applicant page and validate for "an organisation"
    And   I validate choose council page for "yourself" and select a council in "scotland"
    And   I validate local authority page for "yourself" in "scotland"



