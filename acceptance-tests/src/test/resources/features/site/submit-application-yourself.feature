@SubmitApplicationYourself
Feature: DFT Blue badge Citizen app new application
  As a citizen user I want to be able to create a new application for myself

  Scenario: Create a successfull new application for myself
    Given I navigate to the "applicant" page
    Then  I should see the page titled "Who are you applying for? - GOV.UK Apply for a Blue Badge"
    And   I click on element "applicantType.label.YOURSELF"
    And   I can click on "Continue"

    Then  I should see the page titled "Choose your local council - GOV.UK Apply for a Blue Badge"
    When  I type "Worcester" for "councilShortCode" field by id
    And   I select "Worcester city council" from autosuggest council list
    And   I can click on "Continue"

    Then  I should see the page titled "Your issuing authority - GOV.UK Apply for a Blue Badge"
    And   I should see "Worcestershire county council" text on the page
    And   I can click on "Continue"

    Then  I should see the page titled "What's your name? - GOV.UK Apply for a Blue Badge"
    When  I type "Tom Richardson" for "fullName" field by id
    And   I select an option "hasBirthName.no"
    And   I can click on "Continue"

    Then  I should see the page titled "Describe health conditions - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Describe any health conditions that affect your mobility"
    When  I type "Sample health condition" for "description-of-conditions" field by id
    And   I can click on "Continue"

    Then  I should see the page titled "Declaration - GOV.UK Apply for a Blue Badge"
    And   I should see the content "I agree to this declaration"
    And   I select option "declaration.option"
    And   I can click on "Continue"

    Then  I should see the page titled "Application submitted - GOV.UK Apply for a Blue Badge"


  Scenario: Displays error when required fields are not filled
    Given I navigate to the "applicant" page
    Then  I should see the page titled "Who are you applying for? - GOV.UK Apply for a Blue Badge"
    And   I can click on "Continue"
    Then  I should see the page titled "Who are you applying for? - GOV.UK Apply for a Blue Badge"
    And   I should see error summary box
    And   I click on element "applicantType.label.YOURSELF"
    And   I can click on "Continue"

    Then  I should see the page titled "Choose your local council - GOV.UK Apply for a Blue Badge"
    And   I can click on "Continue"
    Then  I should see the page titled "Choose your local council - GOV.UK Apply for a Blue Badge"
    And   I should see error summary box
    When  I type "Worcester" for "councilShortCode" field by id
    And   I select "Worcester city council" from autosuggest council list
    And   I can click on "Continue"
    Then  I should see the page titled "Your issuing authority - GOV.UK Apply for a Blue Badge"
    And   I should see "Worcestershire county council" text on the page
    And   I can click on "Continue"

    Then  I should see the page titled "What's your name? - GOV.UK Apply for a Blue Badge"
    And   I can click on "Continue"
    Then  I should see the page titled "What's your name? - GOV.UK Apply for a Blue Badge"
    And   I should see error summary box
    When  I type "Tom Richardson" for "fullName" field by id
    And   I select an option "hasBirthName.yes"
    And   I can click on "Continue"

    Then  I should see the page titled "What's your name? - GOV.UK Apply for a Blue Badge"
    And   I should see error summary box
    When  I type "Tom Richardson BirthName" for "birthName" field by id
    And   I can click on "Continue"
    Then  I should see the page titled "Describe health conditions - GOV.UK Apply for a Blue Badge"
    And   I can click on "Continue"
    Then  I should see the page titled "Describe health conditions - GOV.UK Apply for a Blue Badge"
    And   I should see error summary box
    When  I type "Sample health condition" for "description-of-conditions" field by id
    And   I can click on "Continue"

    Then  I should see the page titled "Declaration - GOV.UK Apply for a Blue Badge"
    And   I can click on "Continue"
    Then  I should see the page titled "Declaration - GOV.UK Apply for a Blue Badge"
    And   I should see error summary box
    And   I should see the content "I agree to this declaration"
    And   I select option "declaration.option"
    And   I can click on "Continue"
    Then  I should see the page titled "Application submitted - GOV.UK Apply for a Blue Badge"