@SubmitApplicationAFCSRoute
Feature: DFT Blue badge Citizen app new application - AFCS
  As a citizen user I want to be able to create a new application via AFCS route

  Scenario: Create a successful new application for myself - England - No lump sum
    Given I complete applicant page for "yourself"
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete receive benefit page for "AFRFCS"
    And   I complete lump sum of the AFRFCS Scheme page for "NO"
    And   I complete main reason page for "NONE"
    Then  I should see "You're not" eligible page


  Scenario: Create a successful new application for someone else - England - No lump sum
    Given I complete applicant page for "someone else"
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete receive benefit page for "AFRFCS"
    And   I complete lump sum of the AFRFCS Scheme page for "NO"
    And   I complete main reason page for "NONE"
    Then  I should see "They're not" eligible page


  Scenario: Create a successful new application for myself - England - Yes lump sum - Yes disability
    Given I complete applicant page for "yourself"
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete receive benefit page for "AFRFCS"
    And   I complete lump sum of the AFRFCS Scheme page for "YES"
    And   I complete have permanent disability page for "YES"
    Then  I should see "You're" eligible page
    When  I complete eligible page
    And   I complete what's your name page
    And   I complete date of birth page for "CHILD"
    And   I complete gender page for "Boy"
    And   I complete address page
    And   I complete contact page for "yourself"
    And   I complete declaration page
    Then  I should see the page titled "Application submitted" with GOV.UK suffix


  Scenario: Create a successful new application for someone else - England - Yes lump sum - Yes disability
    Given I complete applicant page for "someone else"
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete receive benefit page for "AFRFCS"
    And   I complete lump sum of the AFRFCS Scheme page for "YES"
    And   I complete have permanent disability page for "YES"
    Then  I should see "They are" eligible page
    When  I complete eligible page
    And   I complete what's your name page
    And   I complete date of birth page for "ADULT"
    And   I complete gender page for "Man"
    And   I complete NI number page
    And   I complete address page
    And   I complete contact page for "someone else"
    And   I complete declaration page
    Then  I should see the page titled "Application submitted" with GOV.UK suffix


  Scenario: Create a successful new application for myself - England - Yes lump sum - No disability
    Given I complete applicant page for "yourself"
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete receive benefit page for "AFRFCS"
    And   I complete lump sum of the AFRFCS Scheme page for "YES"
    And   I complete have permanent disability page for "NO"
    And   I complete main reason page for "NONE"
    Then  I should see "You're not" eligible page


  Scenario: Create a successful new application for someone else - England - Yes lump sum - No disability
    Given I complete applicant page for "someone else"
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete receive benefit page for "AFRFCS"
    And   I complete lump sum of the AFRFCS Scheme page for "YES"
    And   I complete have permanent disability page for "NO"
    And   I complete main reason page for "NONE"
    Then  I should see "They're not" eligible page


  Scenario: Create a successful new application for myself - Wales - No lump sum - Yes MentalDisorder
    Given I complete applicant page for "yourself"
    And   I complete select council page for "wales"
    And   I complete your local authority page
    And   I complete receive benefit page for "AFRFCS"
    And   I complete lump sum of the AFRFCS Scheme page for "NO"
    And   I complete has mental disorder page for "YES"
    Then  I should see "You're" eligible page
    When  I complete eligible page
    And   I complete what's your name page
    And   I complete date of birth page for "CHILD"
    And   I complete gender page for "Boy"
    And   I complete address page
    And   I complete contact page for "yourself"
    And   I complete declaration page
    Then  I should see the page titled "Application submitted" with GOV.UK suffix


  Scenario: Create a successful new application for someone else - Wales - No lump sum - Yes MentalDisorder
    Given I complete applicant page for "someone else"
    And   I complete select council page for "wales"
    And   I complete your local authority page
    And   I complete receive benefit page for "AFRFCS"
    And   I complete lump sum of the AFRFCS Scheme page for "NO"
    And   I complete has mental disorder page for "YES"
    Then  I should see "They are" eligible page
    When  I complete eligible page
    And   I complete what's your name page
    And   I complete date of birth page for "CHILD"
    And   I complete gender page for "Boy"
    And   I complete address page
    And   I complete contact page for "someone else"
    And   I complete declaration page
    Then  I should see the page titled "Application submitted" with GOV.UK suffix


  Scenario: Create a successful new application for myself - Wales - No lump sum - No MentalDisorder
    Given I complete applicant page for "yourself"
    And   I complete select council page for "wales"
    And   I complete your local authority page
    And   I complete receive benefit page for "AFRFCS"
    And   I complete lump sum of the AFRFCS Scheme page for "NO"
    And   I complete has mental disorder page for "NO"
    And   I complete main reason page for "WALKD"
    And   I complete what makes walking difficult page for "HELP"
    Then  I should see "You may be" eligible page

  Scenario: Create a successful new application for someone else - Wales - No lump sum - No MentalDisorder
    Given I complete applicant page for "someone else"
    And   I complete select council page for "wales"
    And   I complete your local authority page
    And   I complete receive benefit page for "AFRFCS"
    And   I complete lump sum of the AFRFCS Scheme page for "NO"
    And   I complete has mental disorder page for "NO"
    And   I complete main reason page for "WALKD"
    And   I complete what makes walking difficult page for "HELP"
    Then  I should see "They may be" eligible page
