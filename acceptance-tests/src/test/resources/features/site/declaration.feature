@Declaration
  Feature: DFT Blue badge Citizen app submitting declaration.
    As a citizen user I want to submit a declaration in order to finish my application.

    Scenario: Verify declaration checkbox is checked
      Given I navigate to the "apply-for-a-badge/declaration" page
      Then  I should see the page titled "Declaration - GOV.UK Apply for a Blue Badge"
      And   I select option "declaration.option"
      And   I can click on "Continue"
      Then  I should see the page titled "Application submitted - GOV.UK Apply for a Blue Badge"

    Scenario: Throws error when declaration checkbox is not checked
      Given I navigate to the "apply-for-a-badge/declaration" page
      Then  I should see the page titled "Declaration - GOV.UK Apply for a Blue Badge"
      And   I can click on "Continue"
      Then  I should see the page titled "Declaration - GOV.UK Apply for a Blue Badge"
      And   I should see errorbox