@SubmitApplicationHIDDENRoute
Feature: DFT Blue badge Citizen app new application - Walking Route
  As a citizen user I want to be able to get information on council details via Walking route

  Scenario: Walking application for yourself and need help selected full application
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "AFRFCS"
    And   I complete lump sum of the AFRFCS Scheme page for "NO"
    And   I complete main reason page for "HIDDEN"
    Then  I should see Hidden Disability page
    When  I click on the Return to the start button
    Then  I complete applicant page for "yourself"
