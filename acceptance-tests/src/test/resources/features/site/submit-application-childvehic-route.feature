@SubmitApplicationCHILDVEHICRoute
Feature: DFT Blue badge Citizen app new application - CHILDVEHIC
  As a citizen user I want to be able to get information on council details via CHILDVEHIC route

  Scenario: Child vehicle application for yourself
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "AFRFCS"
    And   I complete lump sum of the AFRFCS Scheme page for "NO"
    And   I complete main reason page for "CHILDVEHIC"
    Then  I should see "You may be" eligible page
    When  I complete eligible page

    Then  I see the "CHILDVEHIC" task list page
    And   I can click on the "Enter personal details" link
    And   I complete what's your name page
    And   I complete date of birth page for "ADULT"
    And   I complete gender page for "Man"
    And   I complete NI number page
    And   I complete address page
    And   I complete contact page for "yourself"

    Then  I see the "CHILDVEHIC" task list page as Adult
    And   I see task "Enter personal details" as COMPLETED
    And   I can click on the "Describe conditions" link
    And   I complete describe health conditions page

    Then  I see the "CHILDVEHIC" task list page as Adult
    And   I can click on the "Add supporting documents" link
    And   I complete upload "supporting documents page" with a "GIF" document

    Then  I see the "CHILDVEHIC" task list page as Adult
    And   I can click on the "List healthcare professionals" link
    And   I complete the healthcare professionals page for "YES"

    Then  I see the "CHILDVEHIC" task list page as Adult
    And   I can click on the "Prove identity" link
    And   I complete "prove ID" page with no documents

    Then  I see the "CHILDVEHIC" task list page as Adult
    And   I can click on the "Add a photo for the badge" link
    And   I complete "provide photo" page with no documents

    Then  I see the "CHILDVEHIC" task list page as Adult
    And   I can click on the "Agree to declaration" link
    And   I complete declaration page

    Then  I see the "CHILDVEHIC" task list page as Adult
    And   I can click on the "Submit application" link
    And   I complete Submit application page
    Then  I should see page titled "Application submitted" with GOV.UK suffix


  Scenario: Child vehicle application for someone else
    Given I complete applicant page for "someone else"
    And   I skip find council page
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "NO"
    And   I complete receive benefit page for "AFRFCS"
    And   I complete lump sum of the AFRFCS Scheme page for "NO"
    And   I complete main reason page for "CHILDVEHIC"
    Then  I should see "They may be" eligible page


  Scenario: Child vehicle application for yourself - change route to PIP
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "AFRFCS"
    And   I complete lump sum of the AFRFCS Scheme page for "NO"
    And   I complete main reason page for "CHILDVEHIC"
    Then  I should see "You may be" eligible page

    # Change the route to PIP
    And   I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "NO"
    And   I complete receive benefit page for "PIP"
    And   I complete moving around points page for "12"
    Then  I should see "You're" eligible page
    When  I complete eligible page

    Then  I see the "PIP" task list page
    And   I can click on the "Enter personal details" link
    And   I complete what's your name page
    And   I complete date of birth page for "ADULT"
    And   I complete gender page for "Man"
    And   I complete NI number page
    And   I complete address page
    And   I complete contact page for "yourself"

    Then  I see the "PIP" task list page as Adult
    And   I see task "Enter personal details" as COMPLETED
    And   I can click on the "Provide proof of benefit" link
    And   I complete prove benefit page for "yes"
    And   I complete "upload benefit" page with no documents

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Prove identity" link
    And   I complete "prove ID" page with no documents

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Prove address" link
    And   I complete "prove address" page with a "PDF" document

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Add a photo for the badge" link
    And   I complete "provide photo" page with no documents

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Agree to declaration" link
    And   I complete declaration page

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Submit application" link
    And   I complete Submit application page
    Then  I should see page titled "Application submitted" with GOV.UK suffix
