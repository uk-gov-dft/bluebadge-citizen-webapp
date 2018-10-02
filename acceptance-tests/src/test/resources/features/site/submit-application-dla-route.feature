@SubmitApplicationDLARoute
Feature: DFT Blue badge Citizen app new application - DLA
  As a citizen user I want to be able to create a new application via DLA route

  # FOR MYSELF
  Scenario: Create a successful new application for myself via DLA route - Yes
    Given I complete applicant page for "yourself"
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete receive benefit page for "DLA"

    Then  I should see the page titled "Were you awarded the higher rate of the mobility component? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Were you awarded the higher rate of the mobility component?"
    And   I select option "awardedHigherRateMobility.option.true"
    And   I can click on "Continue"

    Then  I should see "You're" eligible page
    When  I complete eligible page
    And   I complete what's your name page
    And   I complete date of birth page for "CHILD"
    And   I complete gender page for "Boy"
    And   I complete contact page for "yourself"
    And   I complete address page
    And   I complete describe health conditions page
    And   I complete declaration page
    Then  I should see .* page titled "Application submitted" with GOV.UK suffix


  Scenario: Create a successful new application for myself via DLA route - No
    Given I complete applicant page for "yourself"
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete receive benefit page for "DLA"

    Then  I should see the page titled "Were you awarded the higher rate of the mobility component? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Were you awarded the higher rate of the mobility component?"
    And   I select option "awardedHigherRateMobility.option.false"
    And   I can click on "Continue"

    And   I complete main reason page for "WALKD"
    And I complete what makes walking difficult page for "HELP"
    Then  I should see "You may be" eligible page
    When  I complete eligible page
    And   I complete what's your name page
    And   I complete date of birth page for "CHILD"
    And   I complete gender page for "Boy"
    And   I complete contact page for "yourself"
    And   I complete address page
    And   I complete describe health conditions page
    And   I complete declaration page
    Then  I should see .* page titled "Application submitted" with GOV.UK suffix

  # FOR SOMEONE ELSE
  Scenario: Create a successful new application for someone else - DLA rout - Yes
    Given I complete applicant page for "someone else"
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete receive benefit page for "DLA"

    Then  I should see the page titled "Were they awarded the higher rate of the mobility component? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Were they awarded the higher rate of the mobility component?"
    And   I select option "awardedHigherRateMobility.option.true"
    And   I can click on "Continue"

    Then  I should see "They are" eligible page
    When  I complete eligible page
    And   I complete what's your name page
    And   I complete date of birth page for "CHILD"
    And   I complete gender page for "Boy"
    And   I complete contact page for "someone else"
    And   I complete address page
    And   I complete describe health conditions page
    And   I complete declaration page
    Then  I should see .* page titled "Application submitted" with GOV.UK suffix

  Scenario: Create a successful new application for someone else - DLA route - No
    Given I complete applicant page for "someone else"
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete receive benefit page for "DLA"

    Then  I should see the page titled "Were they awarded the higher rate of the mobility component? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Were they awarded the higher rate of the mobility component?"
    And   I select option "awardedHigherRateMobility.option.false"
    And   I can click on "Continue"

    And   I complete main reason page for "WALKD"
    And I complete what makes walking difficult page for "HELP"
    Then  I should see "They may be" eligible page
    When  I complete eligible page
    And   I complete what's your name page
    And   I complete date of birth page for "CHILD"
    And   I complete gender page for "Boy"
    And   I complete contact page for "someone else"
    And   I complete address page
    And   I complete describe health conditions page
    And   I complete declaration page
    Then  I should see .* page titled "Application submitted" with GOV.UK suffix
