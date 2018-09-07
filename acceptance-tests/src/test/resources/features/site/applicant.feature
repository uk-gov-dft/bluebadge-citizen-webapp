@SelectApplicant
Feature: DFT Blue badge Citizen app selecting type of applicant
  As a citizen user I want to select type of applicant

  Scenario: Select applicant type
    Given I navigate to the "applicant" page
    Then  I should see the page titled "Who are you applying for? - GOV.UK Apply for a Blue Badge"
    And   I click on element "applicantType.label.YOURSELF"
    And   I can click on "Continue"
    Then  I should see the page titled "Declaration - GOV.UK Apply for a Blue Badge"

  Scenario: Displays error when no applicant type is selected
    Given I navigate to the "applicant" page
    Then  I should see the page titled "Who are you applying for? - GOV.UK Apply for a Blue Badge"
    And   I can click on "Continue"
    Then  I should see the page titled "Who are you applying for? - GOV.UK Apply for a Blue Badge"
    And   I should see error summary box