@PageValidations
Feature: DFT Blue badge Citizen app new application - Page Validations via Walking Route
  As a citizen user I want to be able to see proper page titles and validation messages

  Scenario: Page title and error message validation - yourself - with find council
    Given I navigate to applicant page and validate for "yourself"
    And   I validate find council page for "yourself" and select a postcode in "england"
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
    And   I validate the mobility aids page for a "yourself" application for "YES"
    And   I validate walking time page for a "yourself" application for "FEWMIN"
    And   I validate where can you walk page for a "yourself" application
    And   I validate the supporting documents page for a "yourself" application for "YES"
    And   I validate the treatments page for a "yourself" application for "YES"
    And   I validate the medication page for a "yourself" application for "YES"
    And   I validate the healthcare professional page for a "yourself" application for "YES"

#    Following page To be added for page validations
    And   I complete "prove ID" page with a "PDF" document
    And   I complete "provide photo" page with a "JPG" document
    And   I complete "prove address" page with a "PNG" document
    And   I complete declaration page
    Then  I should see page titled "Application submitted" with GOV.UK suffix

  Scenario: Page title and error message validation - yourself - with choose council
    Given I navigate to applicant page and validate for "yourself"
    And   I skip find council page
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
    And   I validate the mobility aids page for a "yourself" application for "YES"
    And   I validate walking time page for a "yourself" application for "FEWMIN"
    And   I validate where can you walk page for a "yourself" application
    And   I validate the supporting documents page for a "yourself" application for "YES"
    And   I validate the treatments page for a "yourself" application for "YES"
    And   I validate the medication page for a "yourself" application for "YES"
    And   I validate the healthcare professional page for a "yourself" application for "YES"

#    Following page To be added for page validations
    And   I complete "prove ID" page with a "PDF" document
    And   I complete "provide photo" page with a "JPG" document
    And   I complete "prove address" page with a "PNG" document
    And   I complete declaration page
    Then  I should see page titled "Application submitted" with GOV.UK suffix



  Scenario: Page title and error message validation - someone else - with find council
    Given I navigate to applicant page and validate for "someone else"
    And   I validate find council page for "someone else" and select a postcode in "wales"
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
    And   I validate contact details page for a "someone else" application
    And   I validate health conditions page for a "someone else" application
    And   I validate what makes walking difficult page for a "someone else" application for "BALANCE"
    And   I validate the mobility aids page for a "someone else" application for "NO"
    And   I validate walking time page for a "someone else" application for "CANTWALK"
    #And   I complete upload "supporting documents page" with a "GIF" document
    And   I validate the supporting documents page for a "yourself" application for "NO"
    And   I validate the treatments page for a "someone else" application for "NO"
    And   I validate the medication page for a "someone else" application for "YES"
    And   I validate the healthcare professional page for a "someone else" application for "NO"

  Scenario: Page title and error message validation - someone else - with choose council
    Given I navigate to applicant page and validate for "someone else"
    And   I skip find council page
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
    And   I validate contact details page for a "someone else" application
    And   I validate health conditions page for a "someone else" application
    And   I validate what makes walking difficult page for a "someone else" application for "BALANCE"
    And   I validate the mobility aids page for a "someone else" application for "NO"
    And   I validate walking time page for a "someone else" application for "CANTWALK"
    #And   I complete upload "supporting documents page" with a "GIF" document
    And   I validate the supporting documents page for a "yourself" application for "NO"
    And   I validate the treatments page for a "someone else" application for "NO"
    And   I validate the medication page for a "someone else" application for "YES"
    And   I validate the healthcare professional page for a "someone else" application for "NO"


  Scenario: Page title and error message validation - an organisation - with find council
    Given I navigate to applicant page and validate for "an organisation"
    And   I validate find council page for "yourself" and select a postcode in "scotland"
    And   I validate local authority page for "yourself" in "scotland"

  Scenario: Page title and error message validation - an organisation - with choose council
    Given I navigate to applicant page and validate for "an organisation"
    And   I skip find council page
    And   I validate choose council page for "yourself" and select a council in "scotland"
    And   I validate local authority page for "yourself" in "scotland"


