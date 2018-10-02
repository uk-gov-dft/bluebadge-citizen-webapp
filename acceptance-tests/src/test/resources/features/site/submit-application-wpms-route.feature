@SubmitApplicationWPMSRoute
Feature: DFT Blue badge Citizen app new application - WPMS or not listed benifits
  As a citizen user I want to be able to create a new application via WPMS route or not listed benifits

  Scenario: Create a successful new application for myself - WPMS route
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

    Then  I should see the page titled "Do you receive any of these benefits? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Do you receive any of these benefits?"
    And   I select option "benefitType.option.WPMS"
    And   I can click on "Continue"

    Then  I should see the page titled "You're eligible for a Blue Badge - GOV.UK Apply for a Blue Badge"
    And   I should see the title "You're eligible for a Blue Badge"
    And   I can click on "Start application"

    Then  I should see the page titled "What's your name? - GOV.UK Apply for a Blue Badge"
    When  I type "Tom Richardson" for "fullName" field by id
    And   I select an option "hasBirthName.no"
    And   I can click on "Continue"

    Then  I should see the page titled "What's your date of birth - GOV.UK Apply for a Blue Badge"
    And   I should see the title "What's your date of birth?"
    When  I type day as "02" month as "08" and year as "1966" for applicant's date of birth
    And   I can click on "Continue"

    Then  I should see the page titled "What's your gender? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "What's your gender?"
    When  I select an option "gender.MALE"
    And   I can click on "Continue"

    And   I complete NI number page
    And   I complete address page
    And   I complete contact page for "yourself"

    Then  I should see the page titled "Declaration - GOV.UK Apply for a Blue Badge"
    And   I should see the content "I agree to this declaration"
    And   I select option "declaration.option"
    And   I can click on "Continue"

    Then  I should see the page titled "Application submitted - GOV.UK Apply for a Blue Badge"

  ##################################################################################################


  Scenario: Create a successful new application for someone else - WPMS route
    Given I navigate to the "applicant" page
    Then  I should see the page titled "Who are you applying for? - GOV.UK Apply for a Blue Badge"
    And   I click on element "applicantType.label.SOMEONE_ELSE"
    And   I can click on "Continue"

    Then  I should see the page titled "Choose their local council - GOV.UK Apply for a Blue Badge"
    When  I type "Worcester" for "councilShortCode" field by id
    And   I select "Worcester city council" from autosuggest council list
    And   I can click on "Continue"

    Then  I should see the page titled "Their issuing authority - GOV.UK Apply for a Blue Badge"
    And   I should see "Worcestershire county council" text on the page
    And   I can click on "Continue"

    Then  I should see the page titled "Do they receive any of these benefits? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Do they receive any of these benefits?"
    And   I select option "benefitType.option.WPMS"
    And   I can click on "Continue"

    Then  I should see the page titled "They are eligible for a Blue Badge - GOV.UK Apply for a Blue Badge"
    And   I should see the title "They are eligible for a Blue Badge"
    And   I can click on "Start application"

    Then  I should see the page titled "What's their name? - GOV.UK Apply for a Blue Badge"
    When  I type "Tom Richardson" for "fullName" field by id
    And   I select an option "hasBirthName.no"
    And   I can click on "Continue"

    Then  I should see the page titled "What's their date of birth - GOV.UK Apply for a Blue Badge"
    When  I type day as "02" month as "08" and year as "1966" for applicant's date of birth
    And   I can click on "Continue"

    Then  I should see the page titled "What's their gender? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "What's their gender?"
    When  I select an option "gender.MALE"
    And   I can click on "Continue"

    And   I complete NI number page
    And   I complete address page
    And   I complete contact page for "someone else"

    Then  I should see the page titled "Declaration - GOV.UK Apply for a Blue Badge"
    And   I should see the content "They agree to this declaration"
    And   I select option "declaration.option"
    And   I can click on "Continue"

    Then  I should see the page titled "Application submitted - GOV.UK Apply for a Blue Badge"

  ##################################################################################################


  Scenario: Create a successful new application for myself - not listed benefits
    Given I complete applicant page for "yourself"
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete receive benefit page for "WPMS"
    Then  I should see "You're" eligible page
    When  I complete eligible page
    And   I complete what's your name page
    And   I complete date of birth page for "CHILD"
    And   I complete gender page for "Boy"
    And   I complete address page
    And   I complete contact page for "yourself"
    And   I complete declaration page
    Then  I should see .* page titled "Application submitted" with GOV.UK suffix


  Scenario: Create a successful new application for someone else - Not listed benefits
    Given I complete applicant page for "someone else"
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete receive benefit page for "WPMS"
    Then  I should see "They are" eligible page
    When  I complete eligible page
    And   I complete what's your name page
    And   I complete date of birth page for "ADULT"
    And   I complete gender page for "Man"
    And   I complete NI number page
    And   I complete address page
    And   I complete contact page for "someone else"
    And   I complete declaration page
    Then  I should see .* page titled "Application submitted" with GOV.UK suffix