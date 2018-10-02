@SubmitApplicationAFCSRoute
Feature: DFT Blue badge Citizen app new application - AFCS
  As a citizen user I want to be able to create a new application via AFCS route

  Scenario: Create a successful new application for myself - England - No lump sum
    Given I complete applicant page for "yourself"
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete receive benefit page for "AFRFCS"

    Then  I should see the page titled "Have you received a lump sum payment within tariff levels 1 to 8 of the Armed Forces Compensation Scheme? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Have you received a lump sum payment within tariff levels 1 to 8 of the Armed Forces Compensation Scheme?"
    And   I select option "hasReceivedCompensation.option.no"
    And   I can click on "Continue"

    And   I complete main reason page for "NONE"
    Then  I should see "You're not" eligible page


  Scenario: Create a successful new application for someone else - England - No lump sum
    Given I complete applicant page for "someone else"
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete receive benefit page for "AFRFCS"

    Then  I should see the page titled "Have they received a lump sum payment within tariff levels 1 to 8 of the Armed Forces Compensation Scheme? - GOV.UK Apply for a Blue Badge"
    And   I select option "hasReceivedCompensation.option.no"
    And   I can click on "Continue"

    And   I complete main reason page for "NONE"
    Then  I should see "They're not" eligible page

  Scenario: Create a successful new application for myself - England - Yes lump sum - Yes disability
    Given I complete applicant page for "yourself"
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete receive benefit page for "AFRFCS"

    Then  I should see the page titled "Have you received a lump sum payment within tariff levels 1 to 8 of the Armed Forces Compensation Scheme? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Have you received a lump sum payment within tariff levels 1 to 8 of the Armed Forces Compensation Scheme?"
    And   I select option "hasReceivedCompensation.option.yes"
    And   I can click on "Continue"

    Then  I should see the page titled "Have you been certified as having a permanent and substantial disability? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Have you been certified as having a permanent and substantial disability?"
    And   I select option "hasDisability.option.yes"
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
    Then  I should see the page titled "Application submitted - GOV.UK Apply for a Blue Badge"


  Scenario: Create a successful new application for someone else - England - Yes lump sum - Yes disability
    Given I complete applicant page for "someone else"
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete receive benefit page for "AFRFCS"

    Then  I should see the page titled "Have they received a lump sum payment within tariff levels 1 to 8 of the Armed Forces Compensation Scheme? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Have they received a lump sum payment within tariff levels 1 to 8 of the Armed Forces Compensation Scheme?"
    And   I select option "hasReceivedCompensation.option.yes"
    And   I can click on "Continue"

    Then  I should see the page titled "Have they been certified as having a permanent and substantial disability? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Have they been certified as having a permanent and substantial disability?"
    And   I select option "hasDisability.option.yes"
    And   I can click on "Continue"

    Then  I should see "They are" eligible page
    When  I complete eligible page
    And   I complete what's your name page
    And   I complete date of birth page for "CHILD"
    And   I complete gender page for "Boy"
    And   I complete address page
    And   I complete contact page for "someone else"
    And   I complete describe health conditions page
    And   I complete declaration page
    Then  I should see the page titled "Application submitted - GOV.UK Apply for a Blue Badge"


  Scenario: Create a successful new application for myself - England - Yes lump sum - No disability
    Given I complete applicant page for "yourself"
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete receive benefit page for "AFRFCS"

    Then  I should see the page titled "Have you received a lump sum payment within tariff levels 1 to 8 of the Armed Forces Compensation Scheme? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Have you received a lump sum payment within tariff levels 1 to 8 of the Armed Forces Compensation Scheme?"
    And   I select option "hasReceivedCompensation.option.yes"
    And   I can click on "Continue"

    Then  I should see the page titled "Have you been certified as having a permanent and substantial disability? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Have you been certified as having a permanent and substantial disability?"
    And   I select option "hasDisability.option.no"
    And   I can click on "Continue"

    And   I complete main reason page for "NONE"
    Then  I should see "You're not" eligible page


  Scenario: Create a successful new application for someone else - England - Yes lump sum - No disability
    Given I complete applicant page for "someone else"
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete receive benefit page for "AFRFCS"

    Then  I should see the page titled "Have they received a lump sum payment within tariff levels 1 to 8 of the Armed Forces Compensation Scheme? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Have they received a lump sum payment within tariff levels 1 to 8 of the Armed Forces Compensation Scheme?"
    And   I select option "hasReceivedCompensation.option.yes"
    And   I can click on "Continue"

    Then  I should see the page titled "Have they been certified as having a permanent and substantial disability? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Have they been certified as having a permanent and substantial disability?"
    And   I select option "hasDisability.option.no"
    And   I can click on "Continue"

    And   I complete main reason page for "NONE"
    Then  I should see "They're not" eligible page




  Scenario: Create a successful new application for myself - Wales - No lump sum - Yes MentalDisorder
    Given I complete applicant page for "yourself"
    And   I complete select council page for "wales"
    And   I complete your local authority page
    And   I complete receive benefit page for "AFRFCS"

    Then  I should see the page titled "Have you received a lump sum payment within tariff levels 1 to 8 of the Armed Forces Compensation Scheme? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Have you received a lump sum payment within tariff levels 1 to 8 of the Armed Forces Compensation Scheme?"
    And   I select option "hasReceivedCompensation.option.no"
    And   I can click on "Continue"

    Then  I should see the page titled "Do you receive tariff level 6 of the Armed Forces Compensation Scheme for a permanent mental disorder? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Do you receive tariff level 6 of the Armed Forces Compensation Scheme for a permanent mental disorder?"
    And   I select option "hasMentalDisorder.option.yes"
    And   I can click on "Continue"

    Then  I should see "You're" eligible page
    When  I complete eligible page
    And   I complete what's your name page
    And   I complete date of birth page for "CHILD"
    And   I complete gender page for "Boy"
    And   I complete address page
    And   I complete contact page for "yourself"
    And   I complete describe health conditions page
    And   I complete declaration page
    Then  I should see the page titled "Application submitted - GOV.UK Apply for a Blue Badge"



  Scenario: Create a successful new application for someone else - Wales - No lump sum - Yes MentalDisorder
    Given I complete applicant page for "someone else"
    And   I complete select council page for "wales"
    And   I complete your local authority page
    And   I complete receive benefit page for "AFRFCS"

    Then  I should see the page titled "Have they received a lump sum payment within tariff levels 1 to 8 of the Armed Forces Compensation Scheme? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Have they received a lump sum payment within tariff levels 1 to 8 of the Armed Forces Compensation Scheme?"
    And   I select option "hasReceivedCompensation.option.no"
    And   I can click on "Continue"

    Then  I should see the page titled "Do they receive tariff level 6 of the Armed Forces Compensation Scheme for a permanent mental disorder? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Do they receive tariff level 6 of the Armed Forces Compensation Scheme for a permanent mental disorder?"
    And   I select option "hasMentalDisorder.option.yes"
    And   I can click on "Continue"

    Then  I should see "They are" eligible page
    When  I complete eligible page
    And   I complete what's your name page
    And   I complete date of birth page for "CHILD"
    And   I complete gender page for "Boy"
    And   I complete address page
    And   I complete contact page for "someone else"
    And   I complete describe health conditions page
    And   I complete declaration page
    Then  I should see the page titled "Application submitted - GOV.UK Apply for a Blue Badge"


  Scenario: Create a successful new application for myself - Wales - No lump sum - No MentalDisorder
    Given I complete applicant page for "yourself"
    And   I complete select council page for "wales"
    And   I complete your local authority page
    And   I complete receive benefit page for "AFRFCS"

    Then  I should see the page titled "Have you received a lump sum payment within tariff levels 1 to 8 of the Armed Forces Compensation Scheme? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Have you received a lump sum payment within tariff levels 1 to 8 of the Armed Forces Compensation Scheme?"
    And   I select option "hasReceivedCompensation.option.no"
    And   I can click on "Continue"

    Then  I should see the page titled "Do you receive tariff level 6 of the Armed Forces Compensation Scheme for a permanent mental disorder? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Do you receive tariff level 6 of the Armed Forces Compensation Scheme for a permanent mental disorder?"
    And   I select option "hasMentalDisorder.option.no"
    And   I can click on "Continue"

    And   I complete main reason page for "WALKD"
    And   I complete what makes walking difficult page for "HELP"

    Then  I should see "You may be" eligible page
    When  I complete eligible page
    And   I complete what's your name page
    And   I complete date of birth page for "CHILD"
    And   I complete gender page for "Boy"
    And   I complete address page
    And   I complete contact page for "yourself"
    And   I complete describe health conditions page
    And   I complete declaration page
    Then  I should see the page titled "Application submitted - GOV.UK Apply for a Blue Badge"


  Scenario: Create a successful new application for someone else - Wales - No lump sum - No MentalDisorder
    Given I complete applicant page for "someone else"
    And   I complete select council page for "wales"
    And   I complete your local authority page
    And   I complete receive benefit page for "AFRFCS"

    Then  I should see the page titled "Have they received a lump sum payment within tariff levels 1 to 8 of the Armed Forces Compensation Scheme? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Have they received a lump sum payment within tariff levels 1 to 8 of the Armed Forces Compensation Scheme?"
    And   I select option "hasReceivedCompensation.option.no"
    And   I can click on "Continue"

    Then  I should see the page titled "Do they receive tariff level 6 of the Armed Forces Compensation Scheme for a permanent mental disorder? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Do they receive tariff level 6 of the Armed Forces Compensation Scheme for a permanent mental disorder?"
    And   I select option "hasMentalDisorder.option.no"
    And   I can click on "Continue"

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
    And   I complete declaration page
    Then  I should see the page titled "Application submitted - GOV.UK Apply for a Blue Badge"
