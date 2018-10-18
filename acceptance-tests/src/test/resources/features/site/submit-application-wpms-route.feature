@SubmitApplicationWPMSRoute
Feature: DFT Blue badge Citizen app new application - WPMS or not listed benifits
  As a citizen user I want to be able to create a new application via WPMS route or not listed benifits

  Scenario: Create a successful new application for myself - WPMS route
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
    Then  I should see page titled "Application submitted" with GOV.UK suffix


  Scenario: Create a successful new application for someone else - WPMS route
    Given I complete applicant page for "someone else"
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete receive benefit page for "WPMS"
    Then  I should see "They are" eligible page