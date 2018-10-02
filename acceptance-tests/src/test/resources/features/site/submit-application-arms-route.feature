@SubmitApplicationARMSRoute
Feature: DFT Blue badge Citizen app new application - ARMS
  As a citizen user I want to be able to get information on council details via ARMS route

  Scenario: Arms application for yourself
    Given I complete applicant page for "yourself"
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete receive benefit page for "AFRFCS"

    Then  I should see the page titled "Have you received a lump sum payment within tariff levels 1 to 8 of the Armed Forces Compensation Scheme? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Have you received a lump sum payment within tariff levels 1 to 8 of the Armed Forces Compensation Scheme?"
    And   I select option "hasReceivedCompensation.option.no"
    And   I can click on "Continue"

    And   I complete main reason page for "ARMS"
    Then  I should see "You may be" eligible page
    When  I complete eligible page
    And   I complete what's your name page
    And   I complete date of birth page for "CHILD"
    And   I complete gender page for "Boy"
    And   I complete address page
    And   I complete contact page for "yourself"
    And   I complete describe health conditions page
    And   I complete declaration page
    Then  I should see .* page titled "Application submitted" with GOV.UK suffix


  Scenario: Arms application for someone else
    Given I complete applicant page for "someone else"
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete receive benefit page for "AFRFCS"

    Then  I should see the page titled "Have they received a lump sum payment within tariff levels 1 to 8 of the Armed Forces Compensation Scheme? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Have they received a lump sum payment within tariff levels 1 to 8 of the Armed Forces Compensation Scheme?"
    And   I select option "hasReceivedCompensation.option.no"
    And   I can click on "Continue"

    And   I complete main reason page for "ARMS"
    Then  I should see "They may be" eligible page
    When  I complete eligible page
    And   I complete what's your name page
    And   I complete date of birth page for "CHILD"
    And   I complete gender page for "Boy"
    And   I complete address page
    And   I complete contact page for "someone else"
    And   I complete describe health conditions page
    And   I complete declaration page
    Then  I should see .* page titled "Application submitted" with GOV.UK suffix



