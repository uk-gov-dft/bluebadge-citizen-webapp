@SubmitApplicationWALKDRoute
Feature: DFT Blue badge Citizen app new application - Walking Route
  As a citizen user I want to be able to get information on council details via Walking route

  Scenario: Walking application for yourself and need help selected full application
    Given I complete applicant page for "yourself"
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
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
    And   I complete the mobility aids page for "NO"
    And   I complete the walking time page with option "LESSMIN"
    And   I complete where can you walk page
    And   I complete upload "supporting documents page" with a "PNG" document
    And   I complete the treatments page for "YES"
    And   I complete the medications page for "YES"
    And   I complete the healthcare professionals page for "YES"
    And   I complete "prove ID" page with a "PNG" document
    And   I complete "provide photo" page with a "PNG" document
    And   I complete declaration page
    Then  I should see page titled "Application submitted" with GOV.UK suffix

  Scenario: Walking application for yourself, where the applicant can't walk - full application
    Given I complete applicant page for "yourself"
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
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
    And   I complete the mobility aids page for "NO"
    And   I complete the walking time page with option "CANTWALK"
    And   I complete the treatments page for "YES"
    And   I complete the medications page for "YES"
    And   I complete the healthcare professionals page for "YES"
    And   I complete "prove ID" page with a "PDF" document
    And   I complete "provide photo" page with a "JPG" document
    And   I complete declaration page
    Then  I should see page titled "Application submitted" with GOV.UK suffix

  Scenario: Walking application for someone else need help selected full application, and council in England
    Given I complete applicant page for "someone else"
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "NO"
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
    And   I complete the mobility aids page for "YES"
    And   I complete the walking time page with option "LESSMIN"
    And   I complete where can you walk page
    And   I complete upload "supporting documents page" with a "PNG" document
    And   I complete the treatments page for "YES"
    And   I complete the medications page for "YES"
    And   I complete the healthcare professionals page for "YES"
    And   I complete "prove ID" page with no documents
    And   I complete "provide photo" page with no documents
    And   I complete declaration page
    Then  I should see page titled "Application submitted" with GOV.UK suffix

  Scenario: Walking application for yourself, need help selected full application, and council in Scotland
    Given I complete applicant page for "yourself"
    And   I complete select council page for "scotland"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "AFRFCS"
    And   I complete lump sum of the AFRFCS Scheme page for "NO"
    And   I complete main reason page for "WALKD"
    And   I complete what makes walking difficult page for "PLAN"
    Then  I should see "You may be" eligible page
    When  I complete eligible page
    And   I complete what's your name page
    And   I complete date of birth page for "ADULT"
    And   I complete gender page for "Man"
    And   I complete NI number page without a NI
    And   I complete address page
    And   I complete contact page for "yourself"
    And   I complete describe health conditions page
    And   I complete the what makes walking difficult page for "STRUGGLE"
    And   I complete the mobility aids page for "YES"
    And   I complete the walking time page with option "CANTWALK"
    And   I complete the treatments page for "YES"
    And   I complete the medications page for "YES"
    And   I complete the healthcare professionals page for "YES"
    And   I complete "prove ID" page with no documents
    And   I complete "provide photo" page with no documents
    And   I complete declaration page
    Then  I should see page titled "Application submitted" with GOV.UK suffix

  Scenario: Walking application for yourself, need help selected full application, and council in Wales
    Given I complete applicant page for "yourself"
    And   I complete select council page for "wales"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES BUT DON'T KNOW"
    And   I complete receive benefit page for "AFRFCS"
    And   I complete lump sum of the AFRFCS Scheme page for "NO"
    And   I complete has mental disorder page for "NO"
    And   I complete main reason page for "WALKD"
    And   I complete what makes walking difficult page for "PLAN"
    Then  I should see "You may be" eligible page
    When  I complete eligible page
    And   I complete what's your name page
    And   I complete date of birth page for "ADULT"
    And   I complete gender page for "Man"
    And   I complete NI number page
    And   I complete address page
    And   I complete contact page for "yourself"
    And   I complete describe health conditions page
    And   I complete the what makes walking difficult page for "STRUGGLE"
    And   I complete the mobility aids page for "YES"
    And   I complete the walking time page with option "LESSMIN"
    And   I complete where can you walk page
    And   I complete upload "supporting documents page" with a "PNG" document
    And   I complete the treatments page for "NO"
    And   I complete the medications page for "YES"
    And   I complete the healthcare professionals page for "YES"
    And   I complete "prove ID" page with no documents
    And   I complete "provide photo" page with no documents
    And   I complete declaration page
    Then  I should see page titled "Application submitted" with GOV.UK suffix

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

