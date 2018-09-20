@SubmitApplicationSomeoneElse
Feature: DFT Blue badge Citizen app new application
  As a citizen user I want to be able to create a new application for someone else

  Scenario: Create a successful new application for someone else
    Given I navigate to the "applicant" page
    Then  I should see the page titled "Who are you applying for? - GOV.UK Apply for a Blue Badge"
    And   I click on element "applicantType.label.SOMEONE_ELSE"
    And   I can click on "Continue"
    Then  I should see the page titled "Do they receive any of these benefits? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Do they receive any of these benefits?"
    And   I select option "benefitType.option.PIP"
    And   I can click on "Continue"
    Then  I should see the page titled "They may be eligible for a Blue Badge - GOV.UK Apply for a Blue Badge"
    And   I should see the title "They may be eligible for a Blue Badge"
    And   I can click on "Start Application"
    Then  I should see the page titled "What's their name? - GOV.UK Apply for a Blue Badge"
    When  I type "Tom Richardson" for "fullName" field by id
    And   I select an option "hasBirthName.no"
    And   I can click on "Continue"
    Then  I should see the page titled "Describe health conditions - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Describe any health conditions that affect their mobility"
    When  I type "Sample health condition" for "description-of-conditions" field by id
    And   I can click on "Continue"
    Then  I should see the page titled "Declaration - GOV.UK Apply for a Blue Badge"
    And   I should see the content "They agree to this declaration"
    And   I select option "declaration.option"
    And   I can click on "Continue"
    Then  I should see the page titled "Application submitted - GOV.UK Apply for a Blue Badge"
