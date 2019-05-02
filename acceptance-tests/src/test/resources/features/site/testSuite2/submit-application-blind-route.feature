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

    Then  I see the "BLIND" task list page
    And   I can click on the "Enter personal details" link
    And   I complete what's your name page
    And   I complete date of birth page for "CHILD"
    And   I complete gender page for "Boy"
    And   I complete address page
    And   I complete contact page for "yourself"

    Then  I see the "BLIND" task list page as Child
    And   I see task "Enter personal details" as COMPLETED
    And   I can click on the "Provide proof of visual impairment" link
    And   I complete registered page for "yes"
    And   I complete permission page for "yes"
    And   I complete registered council page for "england"

    Then  I see the "BLIND" task list page as Child
    And   I can click on the "Prove identity" link
    And   I complete "prove ID" page with no documents

    Then  I see the "BLIND" task list page as Child
    And   I can click on the "Add a photo for the badge" link
    And   I complete "provide photo" page with no documents

    Then  I see the "BLIND" task list page as Child
    And   I can click on the "Agree to declaration" link
    And   I complete declaration page

    Then  I see the "BLIND" task list page as Child
    And   I can click on the "Submit application" link
    And   I complete Submit application page
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

    Then  I see the "BLIND" task list page
    And   I can click on the "Enter personal details" link
    And   I complete what's your name page
    And   I complete date of birth page for "CHILD"
    And   I complete gender page for "Boy"
    And   I complete address page
    And   I complete contact page for "yourself"

    Then  I see the "BLIND" task list page as Child
    And   I see task "Enter personal details" as COMPLETED
    And   I can click on the "Provide proof of visual impairment" link
    And   I complete registered page for "yes"
    And   I complete permission page for "no"

    Then  I see the "BLIND" task list page as Child
    And   I can click on the "Prove identity" link
    And   I complete "prove ID" page with no documents

    Then  I see the "BLIND" task list page as Child
    And   I can click on the "Add a photo for the badge" link
    And   I complete "provide photo" page with no documents

    Then  I see the "BLIND" task list page as Child
    And   I can click on the "Agree to declaration" link
    And   I complete declaration page

    Then  I see the "BLIND" task list page as Child
    And   I can click on the "Submit application" link
    And   I complete Submit application page
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

    Then  I see the "BLIND" task list page
    And   I can click on the "Enter personal details" link
    And   I complete what's your name page
    And   I complete date of birth page for "CHILD"
    And   I complete gender page for "Boy"
    And   I complete address page
    And   I complete contact page for "yourself"

    Then  I see the "BLIND" task list page as Child
    And   I see task "Enter personal details" as COMPLETED
    And   I can click on the "Provide proof of visual impairment" link
    And   I complete registered page for "no"

    Then  I see the "BLIND" task list page as Child
    And   I can click on the "Prove identity" link
    And   I complete "prove ID" page with no documents

    Then  I see the "BLIND" task list page as Child
    And   I can click on the "Add a photo for the badge" link
    And   I complete "provide photo" page with no documents

    Then  I see the "BLIND" task list page as Child
    And   I can click on the "Agree to declaration" link
    And   I complete declaration page

    Then  I see the "BLIND" task list page as Child
    And   I can click on the "Submit application" link
    And   I complete Submit application page
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


