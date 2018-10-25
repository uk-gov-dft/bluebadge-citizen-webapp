@PageValidationsYourself
Feature: DFT Blue badge Citizen app new application - Page Validations via Walking Route
  As a citizen user I want to be able to see proper page titles and validation messages

  Scenario: Page title and error message validation - yourself
    Given I navigate to applicant page and validate for "yourself"
    And   I validate choose council page for "yourself" and select a council in "england"


  Scenario: Page title and error message validation - someone else
    Given I navigate to applicant page and validate for "someone else"
    And   I validate choose council page for "someone else" and select a council in "england"
