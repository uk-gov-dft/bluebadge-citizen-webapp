@SubmitExistingBadge
Feature: DFT Blue badge Citizen app new application - Existing Badge
  As a citizen user I want to be able to state if I have an existing badge

  Scenario: State that I do not have an existing badge
    Given I complete applicant page for "yourself"
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "No"
    And   I complete receive benefit page for "AFRFCS"
    And   I complete lump sum of the AFRFCS Scheme page for "NO"
    And   I complete main reason page for "NONE"
    Then  I should see "You're not" eligible page

  Scenario: State that I do not have an existing badge
    Given I complete applicant page for "yourself"
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "Yes"
    And   I complete receive benefit page for "AFRFCS"
    And   I complete lump sum of the AFRFCS Scheme page for "NO"
    And   I complete main reason page for "NONE"
    Then  I should see "You're not" eligible page