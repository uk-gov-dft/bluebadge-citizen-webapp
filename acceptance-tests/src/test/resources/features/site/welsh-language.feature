@Welsh
Feature: Apply for a blue badge in Welsh feature

  As a citizen I should be able to apply for a blue badge in the Welsh language

  Scenario: Language changes to Welsh
    Given   I navigate to the "applicant" page
    Then    I should see the page titled "Who are you applying for? - GOV.UK Apply for a Blue Badge"
    And     I can click on "Cymraeg"
    Then    I should see the page titled "Pwy ydych chi'n gwneud cais amdano? - GOV.UK Apply for a Blue Badge"
    Given   I complete applicant page for "yourself"
    Then    I should see the page titled "Dewiswch eich cyngor cyngor - GOV.UK Apply for a Blue Badge"
    And     I can click on "English"
    Then    I should see the page titled "Choose your local council - GOV.UK Apply for a Blue Badge"
