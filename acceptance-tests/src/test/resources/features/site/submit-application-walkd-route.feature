@SubmitApplicationWALKDRoute
Feature: DFT Blue badge Citizen app new application - TERMILL
  As a citizen user I want to be able to get information on council details via TERMILL route

  Scenario: Walking application for yourself and need help selected full application
    Given I complete applicant page for "yourself"
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete receive benefit page for "AFRFCS"

    Then  I should see the page titled "Have you received a lump sum payment within tariff levels 1 to 8 of the Armed Forces Compensation Scheme? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Have you received a lump sum payment within tariff levels 1 to 8 of the Armed Forces Compensation Scheme?"
    And   I select option "hasReceivedCompensation.option.no"
    And   I can click on "Continue"

    And   I complete main reason page for "WALKD"
    And   I complete what makes walking difficult page for "HELP"
    Then  I should see "You may be" eligible page
    When  I complete eligible page
    And   I complete what's your name page
    And   I complete date of birth page for "CHILD"
    And   I complete gender page for "Boy"
    And   I complete contact page for "yourself"

    And   I complete describe health conditions page
    And   I complete declaration page
    Then  I should see .* page titled "Application submitted" with GOV.UK suffix


  Scenario: Walking application for someone else need help selected full application
    Given I complete applicant page for "someone else"
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete receive benefit page for "AFRFCS"

    Then  I should see the page titled "Have they received a lump sum payment within tariff levels 1 to 8 of the Armed Forces Compensation Scheme? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Have they received a lump sum payment within tariff levels 1 to 8 of the Armed Forces Compensation Scheme?"
    And   I select option "hasReceivedCompensation.option.no"
    And   I can click on "Continue"

    And   I complete main reason page for "WALKD"
    And   I complete what makes walking difficult page for "HELP"
    Then  I should see "They may be" eligible page
    When  I complete eligible page
    And   I complete what's your name page
    And   I complete date of birth page for "CHILD"
    And   I complete gender page for "Boy"
    And   I complete contact page for "someone else"

    And   I complete describe health conditions page
    And   I complete declaration page
    Then  I should see .* page titled "Application submitted" with GOV.UK suffix

  Scenario: Walking application for yourself and painful selected check screen flow
    Given I complete application up to the Main Reason page for "yourself"
    Then  I should see the page titled "What is the main reason you need a badge?" with GOV.UK suffix
    And   I should see the title "What is the main reason you need a badge?"
    And   I can click on "Continue"
    And   I should see error summary box
    And   I select option "mainReasonOption.option.WALKD"
    And   I can click on "Continue"

    Then  I should see the page titled "What makes walking difficult for you?" with GOV.UK suffix
    And   I can click on "Continue"
    And   I should see error summary box
    And   I select option "walkingDifficulty.option.PAIN"
    And   I can click on "Continue"

    Then  I should see the page titled "You may be eligible for a Blue Badge" with GOV.UK suffix
    And   I should see the title "You may be eligible for a Blue Badge"
    And   I can click on "Start application"

  Scenario: Walking application for yourself and dangerous selected check screen flow
    Given I complete application up to the Main Reason page for "yourself"
    Then  I should see the page titled "What is the main reason you need a badge?" with GOV.UK suffix
    And   I should see the title "What is the main reason you need a badge?"
    And   I can click on "Continue"
    And   I should see error summary box
    And   I select option "mainReasonOption.option.WALKD"
    And   I can click on "Continue"

    Then  I should see the page titled "What makes walking difficult for you?" with GOV.UK suffix
    And   I can click on "Continue"
    And   I should see error summary box
    And   I select option "walkingDifficulty.option.DANGEROUS"
    And   I can click on "Continue"

    Then  I should see the page titled "You may be eligible for a Blue Badge" with GOV.UK suffix
    And   I should see the title "You may be eligible for a Blue Badge"
    And   I can click on "Start application"

  Scenario: Walking application for yourself with none selected for walking difficulty
    Given I complete application up to the Main Reason page for "yourself"
    Then  I should see the page titled "What is the main reason you need a badge?" with GOV.UK suffix
    And   I should see the title "What is the main reason you need a badge?"
    And   I can click on "Continue"
    And   I should see error summary box
    And   I select option "mainReasonOption.option.WALKD"
    And   I can click on "Continue"

    Then  I should see the page titled "What makes walking difficult for you?" with GOV.UK suffix
    And   I can click on "Continue"
    And   I should see error summary box
    And   I select option "walkingDifficulty.option.NONE"
    And   I can click on "Continue"

    Then  I should see the page titled "You're not eligible" with GOV.UK suffix

  Scenario: Walking application for someone else with none selected for walking difficulty
    Given I complete application up to the Main Reason page for "someone else"
    Then  I should see the page titled "What is the main reason they need a badge?" with GOV.UK suffix
    And   I should see the title "What is the main reason they need a badge?"
    And   I select option "mainReasonOption.option.WALKD"
    And   I can click on "Continue"

    Then  I should see the page titled "What makes walking difficult for them?" with GOV.UK suffix
    And   I can click on "Continue"
    And   I should see error summary box
    And   I select option "walkingDifficulty.option.NONE"
    And   I can click on "Continue"

    Then  I should see the page titled "They're not eligible" with GOV.UK suffix

