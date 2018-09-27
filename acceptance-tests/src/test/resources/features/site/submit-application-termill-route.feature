@SubmitApplicationTERMILLRoute
Feature: DFT Blue badge Citizen app new application - TERMILL
  As a citizen user I want to be able to get information on council details via TERMILL route

  Scenario: Terminal Illness application for yourself
    Given I complete application up to the Main Reason page for "yourself"
    Then  I should see the page titled "What is the main reason you need a badge?" with GOV.UK suffix
    And   I should see the title "What is the main reason you need a badge?"
    And   I can click on "Continue"
    And   I should see error summary box
    And   I select option "mainReasonOption.option.TERMILL"
    And   I can click on "Continue"

    Then  I should see the page titled "Contact your council" with GOV.UK suffix

  Scenario: Terminal Illness application for someone else
    Given I complete application up to the Main Reason page for "someone else"
    Then  I should see the page titled "What is the main reason they need a badge?" with GOV.UK suffix
    And   I should see the title "What is the main reason they need a badge?"
    And   I select option "mainReasonOption.option.TERMILL"
    And   I can click on "Continue"

    Then  I should see the page titled "Contact their council" with GOV.UK suffix

  Scenario: No main reason myself
    Given I complete application up to the Main Reason page for "yourself"
    Then  I should see the page titled "What is the main reason you need a badge?" with GOV.UK suffix
    And   I should see the title "What is the main reason you need a badge?"
    And   I select option "mainReasonOption.option.NONE"
    And   I can click on "Continue"

    Then  I should see the page titled "You're not eligible" with GOV.UK suffix

  Scenario: No main reason someone else
    Given I complete application up to the Main Reason page for "someone else"
    Then  I should see the page titled "What is the main reason they need a badge?" with GOV.UK suffix
    And   I should see the title "What is the main reason they need a badge?"
    And   I select option "mainReasonOption.option.NONE"
    And   I can click on "Continue"

    Then  I should see the page titled "They're not eligible" with GOV.UK suffix
