@Welsh
Feature: Apply for a blue badge in Welsh feature

  As a citizen I should be able to apply for a blue badge in the Welsh language

  Scenario: Language changes to Welsh
    Given   I navigate to the "applicant" page
    Then    I should see the page titled "Who are you applying for? - Apply for a Blue Badge - GOV.UK"
    And     I can click on "Cymraeg"
    Then    I should see the page titled "Ar ran pwy ydych chi'n gwneud cais? - Gwneud cais am Fathodyn Glas - GOV.UK"
    Given   I complete applicant page for "yourself"
    Then    I should see the page titled "Dewiswch eich cyngor lleol - Gwneud cais am Fathodyn Glas - GOV.UK"
    And     I can click on "English"
    Then    I should see the page titled "Choose your local council - Apply for a Blue Badge - GOV.UK"
