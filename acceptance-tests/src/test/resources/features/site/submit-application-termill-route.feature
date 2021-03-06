@SubmitApplicationTERMILLRoute
Feature: DFT Blue badge Citizen app new application - TERMILL
  As a citizen user I want to be able to get information on council details via TERMILL route

  Scenario: Terminal Illness application for yourself
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "NO"
    And   I complete receive benefit page for "NONE"
    And   I complete main reason page for "TERMILL"
    Then  I should see the page titled "Contact your council" with GOV.UK suffix

  Scenario: Terminal Illness application for someone else
    Given I complete applicant page for "someone else"
    And   I skip find council page
    And   I complete select council page
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES BUT DON'T KNOW"
    And   I complete receive benefit page for "NONE"
    And   I complete main reason page for "TERMILL"
    Then  I should see the page titled "Contact their council" with GOV.UK suffix

  Scenario: No main reason myself
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "NO"
    And   I complete receive benefit page for "NONE"
    And   I complete main reason page for "NONE"
    Then  I should see the page titled "You're not eligible" with GOV.UK suffix

  Scenario: No main reason someone else
    Given I complete applicant page for "someone else"
    And   I skip find council page
    And   I complete select council page
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "NONE"
    And   I complete main reason page for "NONE"
    Then  I should see the page titled "They're not eligible" with GOV.UK suffix
