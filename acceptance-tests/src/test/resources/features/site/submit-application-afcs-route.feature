@SubmitApplicationAFCSRoute
Feature: DFT Blue badge Citizen app new application - AFCS
  As a citizen user I want to be able to create a new application via AFCS route

  Scenario: Create a successful new application for myself - England - No lump sum
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES BUT DON'T KNOW"
    And   I complete receive benefit page for "AFRFCS"
    And   I complete lump sum of the AFRFCS Scheme page for "NO"
    And   I complete main reason page for "NONE"
    Then  I should see "You're not" eligible page


  Scenario: Create a successful new application for someone else - England - No lump sum
    Given I complete applicant page for "someone else"
    And   I skip find council page
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "NO"
    And   I complete receive benefit page for "AFRFCS"
    And   I complete lump sum of the AFRFCS Scheme page for "NO"
    And   I complete main reason page for "NONE"
    Then  I should see "They're not" eligible page


  Scenario: Create a successful new application for myself - England - Yes lump sum - Yes disability
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "AFRFCS"
    And   I complete lump sum of the AFRFCS Scheme page for "YES"
    And   I complete have permanent disability page for "YES"
    Then  I should see "You're" eligible page
    When  I complete eligible page
    Then  I see the "AFRFCS" task list page
    And   I can click on the "Enter personal details" link
    And   I complete what's your name page
    And   I complete date of birth page for "CHILD"
    And   I complete gender page for "Boy"
    And   I complete address page
    And   I complete contact page for "yourself"
    Then  I see the "AFRFCS" task list page as Child
    And   I see task "Enter personal details" as COMPLETED
    And   I can click on the "Prove identity" link
    And   I complete "prove ID" page with no documents
    Then  I see the "AFRFCS" task list page as Child
    And   I can click on the "Add a photo for the badge" link
    And   I complete "provide photo" page with no documents
    Then  I see the "AFRFCS" task list page as Child
    And   I can click on the "Agree to declaration" link
    And   I complete declaration page
    Then  I see the "AFRFCS" task list page as Child
    And   I can click on the "Submit application" link
    And   I complete Submit application page
    Then  I should see the page titled "Application submitted" with GOV.UK suffix


  Scenario: Create a successful new application for someone else - England - Yes lump sum - Yes disability
    Given I complete applicant page for "someone else"
    And   I skip find council page
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "NO"
    And   I complete receive benefit page for "AFRFCS"
    And   I complete lump sum of the AFRFCS Scheme page for "YES"
    And   I complete have permanent disability page for "YES"
    Then  I should see "They're" eligible page


  Scenario: Create a successful new application for myself - England - Yes lump sum - No disability
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "AFRFCS"
    And   I complete lump sum of the AFRFCS Scheme page for "YES"
    And   I complete have permanent disability page for "NO"
    And   I complete main reason page for "NONE"
    Then  I should see "You're not" eligible page


  Scenario: Create a successful new application for someone else - England - Yes lump sum - No disability
    Given I complete applicant page for "someone else"
    And   I skip find council page
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "NO"
    And   I complete receive benefit page for "AFRFCS"
    And   I complete lump sum of the AFRFCS Scheme page for "YES"
    And   I complete have permanent disability page for "NO"
    And   I complete main reason page for "NONE"
    Then  I should see "They're not" eligible page


  Scenario: Create a successful new application for myself - Wales - No lump sum - Yes MentalDisorder
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page for "wales"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "AFRFCS"
    And   I complete lump sum of the AFRFCS Scheme page for "NO"
    And   I complete has mental disorder page for "YES"
    Then  I should see "You're" eligible page


  Scenario: Create a successful new application for someone else - Wales - No lump sum - Yes MentalDisorder
    Given I complete applicant page for "someone else"
    And   I skip find council page
    And   I complete select council page for "wales"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "NO"
    And   I complete receive benefit page for "AFRFCS"
    And   I complete lump sum of the AFRFCS Scheme page for "NO"
    And   I complete has mental disorder page for "YES"
    Then  I should see "They're" eligible page


  Scenario: Create a successful new application for myself - Wales - No lump sum - No MentalDisorder
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page for "wales"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "NO"
    And   I complete receive benefit page for "AFRFCS"
    And   I complete lump sum of the AFRFCS Scheme page for "NO"
    And   I complete has mental disorder page for "NO"
    And   I complete main reason page for "WALKD"
    And   I complete what makes walking difficult page for "HELP"
    Then  I should see "You may be" eligible page

  Scenario: Create a successful new application for someone else - Wales - No lump sum - No MentalDisorder
    Given I complete applicant page for "someone else"
    And   I skip find council page
    And   I complete select council page for "wales"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "AFRFCS"
    And   I complete lump sum of the AFRFCS Scheme page for "NO"
    And   I complete has mental disorder page for "NO"
    And   I complete main reason page for "WALKD"
    And   I complete what makes walking difficult page for "HELP"
    Then  I should see "They may be" eligible page
