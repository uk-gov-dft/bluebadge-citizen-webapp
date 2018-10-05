@SubmitApplicationWALKDRoute
Feature: DFT Blue badge Citizen app new application - TERMILL
  As a citizen user I want to be able to get information on council details via TERMILL route

  Scenario: Walking application for yourself and need help selected full application
    Given I complete applicant page for "yourself"
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete receive benefit page for "AFRFCS"
    And   I complete lump sum of the AFRFCS Scheme page for "NO"
    And   I complete main reason page for "WALKD"
    And   I complete what makes walking difficult page for "HELP"
    Then  I should see "You may be" eligible page
    When  I complete eligible page
    And   I complete what's your name page
    And   I complete date of birth page for "ADULT"
    And   I complete gender page for "Man"
    And   I complete NI number page
    And   I complete address page
    And   I complete contact page for "yourself"
    And   I complete describe health conditions page
    And   I complete the what makes walking difficult page
    And   I complete the mobility aids page
    And   I complete where can you walk page
    And   I complete declaration page
    Then  I should see .* page titled "Application submitted" with GOV.UK suffix


  Scenario: Walking application for someone else need help selected full application
    Given I complete applicant page for "someone else"
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete receive benefit page for "AFRFCS"
    And   I complete lump sum of the AFRFCS Scheme page for "NO"
    And   I complete main reason page for "WALKD"
    And   I complete what makes walking difficult page for "HELP"
    Then  I should see "They may be" eligible page
    When  I complete eligible page
    And   I complete what's your name page
    And   I complete date of birth page for "CHILD"
    And   I complete gender page for "Boy"
    And   I complete address page
    And   I complete contact page for "someone else"
    And   I complete describe health conditions page
    And   I complete the what makes walking difficult page
    And   I complete the mobility aids page
    And   I complete where can you walk page
    And   I complete declaration page
    Then  I should see .* page titled "Application submitted" with GOV.UK suffix

  Scenario: Walking application for yourself and painful selected check screen flow
    Given I complete application up to the Main Reason page for "yourself"
    And   I complete main reason page for "WALKD"
    And   I complete what makes walking difficult page for "PAIN"

    Then  I should see the page titled "You may be eligible for a Blue Badge" with GOV.UK suffix
    And   I should see the title "You may be eligible for a Blue Badge"
    And   I can click on "Start application"

  Scenario: Walking application for yourself and dangerous selected check screen flow
    Given I complete application up to the Main Reason page for "yourself"
    And   I complete main reason page for "WALKD"
    And   I complete what makes walking difficult page for "DANGEROUS"
    Then  I should see "You may be" eligible page

  Scenario: Walking application for yourself with none selected for walking difficulty
    Given I complete application up to the Main Reason page for "yourself"
    And   I complete main reason page for "WALKD"
    And   I complete what makes walking difficult page for "NONE"
    Then  I should see "You're not" eligible page

  Scenario: Walking application for someone else with none selected for walking difficulty
    Given I complete application up to the Main Reason page for "someone else"
    And   I complete main reason page for "WALKD"
    And   I complete what makes walking difficult page for "NONE"
    Then  I should see "They're not" eligible page

