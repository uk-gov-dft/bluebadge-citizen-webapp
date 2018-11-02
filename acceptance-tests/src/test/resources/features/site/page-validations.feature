@PageValidations
Feature: DFT Blue badge Citizen app new application - Page Validations via Walking Route
  As a citizen user I want to be able to see proper page titles and validation messages

  Scenario: Page title and error message validation - yourself
    Given I navigate to applicant page and validate for "yourself"
    And   I validate choose council page for "yourself" and select a council in "england"
    And   I validate "your" local authority page in "england"
    And   I validate "you" already have a blue badge page for "Yes"
    And   I validate "you" benefit page for "NONE"

  Scenario: Page title and error message validation - someone else
    Given I navigate to applicant page and validate for "someone else"
    And   I validate choose council page for "someone else" and select a council in "wales"
    And   I validate "their" local authority page in "wales"
    And   I validate "they" already have a blue badge page for "No"


  Scenario: Page title and error message validation - an organisation
    Given I navigate to applicant page and validate for "an organisation"
    And   I validate choose council page for "yourself" and select a council in "scotland"
    And   I validate "your" local authority page in "scotland"