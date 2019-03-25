@SubmitApplicationBLINDRoute
Feature: DFT Blue badge Citizen app new application - BLIND
  As a citizen user I want to be able to get information on council details via BLIND route

  Scenario: Blind application for yourself (has registered and has permission)
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "NO"
    And   I complete receive benefit page for "AFRFCS"
    And   I complete lump sum of the AFRFCS Scheme page for "NO"
    And   I complete main reason page for "BLIND"
    Then  I should see "You're" eligible page
    When  I complete eligible page
    And   I complete what's your name page
    And   I complete date of birth page for "CHILD"
    And   I complete gender page for "Boy"
    And   I complete address page
    And   I complete contact page for "yourself"
    And   I complete registered page for "yes"
    And   I complete permission page for "yes"
    And   I complete registered council page for "england"
    And   I complete "prove ID" page with no documents
    And   I complete "provide photo" page with no documents
    And   I complete "prove address" page with a "JPG" document
    And   I complete declaration page
    Then  I should see page titled "Application submitted" with GOV.UK suffix

  Scenario: Blind application for yourself (has registered and but has no permission)
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "NO"
    And   I complete receive benefit page for "AFRFCS"
    And   I complete lump sum of the AFRFCS Scheme page for "NO"
    And   I complete main reason page for "BLIND"
    Then  I should see "You're" eligible page
    When  I complete eligible page
    And   I complete what's your name page
    And   I complete date of birth page for "CHILD"
    And   I complete gender page for "Boy"
    And   I complete address page
    And   I complete contact page for "yourself"
    And   I complete registered page for "yes"
    And   I complete permission page for "no"
    And   I complete "prove ID" page with no documents
    And   I complete "provide photo" page with no documents
    And   I complete "prove address" page with a "GIF" document
    And   I complete declaration page
    Then  I should see page titled "Application submitted" with GOV.UK suffix

  Scenario: Blind application for yourself (has not registered)
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "NO"
    And   I complete receive benefit page for "AFRFCS"
    And   I complete lump sum of the AFRFCS Scheme page for "NO"
    And   I complete main reason page for "BLIND"
    Then  I should see "You're" eligible page
    When  I complete eligible page
    And   I complete what's your name page
    And   I complete date of birth page for "CHILD"
    And   I complete gender page for "Boy"
    And   I complete address page
    And   I complete contact page for "yourself"
    And   I complete registered page for "no"
    And   I complete "prove ID" page with no documents
    And   I complete "provide photo" page with no documents
    And   I complete "prove address" page with a "JPG" document
    And   I complete declaration page
    Then  I should see page titled "Application submitted" with GOV.UK suffix


  Scenario: Blind application for someone else
    Given I complete applicant page for "someone else"
    And   I skip find council page
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "AFRFCS"
    And   I complete lump sum of the AFRFCS Scheme page for "NO"
    And   I complete main reason page for "BLIND"
    Then  I should see "They're" eligible page


