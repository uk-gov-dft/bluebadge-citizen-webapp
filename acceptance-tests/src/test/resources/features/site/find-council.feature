@FindYourCouncil
Feature: DFT Blue badge Citizen app - find council
  As a user
  I want to be advised of my issuing authority based on my postcode
  So that I know for sure who is going to assess my application and hopefully issue my badge

  Scenario: Find your council - yourself - english - skip find council
    Given I complete applicant page for "yourself"
    Then  I should see the page titled "Find your local council" with GOV.UK suffix
    And   I skip find council page
    Then  I should see the page titled "Choose your local council" with GOV.UK suffix
    And   I complete select council page for "england"
    Then  I should see the page titled "Your issuing authority" with GOV.UK suffix
    Then  I can see labelled element "localAuthority" with content "Blackpool borough council"
    When  I complete your local authority page
    Then  I should see the page titled "Do you already have a Blue Badge?" with GOV.UK suffix

  Scenario: Find your council - yourself - english - find council - change council several times in different ways
    Given I complete applicant page for "yourself"
    Then I should see the page titled "Find your local council" with GOV.UK suffix
    When I complete find council page for "yourself" and select a postcode in "england"
    Then I should see the page titled "Your issuing authority" with GOV.UK suffix
    Then I can see labelled element "localAuthority" with content "Blackpool borough council"

    When I can click "thatsNotMyLocalCouncilLink" button
    Then I should see the page titled "Choose your local council" with GOV.UK suffix
    When I complete select council page for "scotland"
    Then I should see the page titled "Your issuing authority" with GOV.UK suffix
    Then I can see labelled element "localAuthority" with content "Aberdeenshire council"
    When I complete your local authority page
    Then I should see the page titled "Do you already have a Blue Badge?" with GOV.UK suffix

    When I go back in the browser
    Then I can see labelled element "localAuthority" with content "Aberdeenshire council"
    When I can click "thatsNotMyLocalCouncilLink" button
    Then I should see the page titled "Choose your local council" with GOV.UK suffix
    When I complete select council page for "wales"
    Then I should see the page titled "Your issuing authority" with GOV.UK suffix
    Then I can see labelled element "localAuthority" with content "Isle of Anglesey county council"
    When I complete your local authority page
    Then I should see the page titled "Do you already have a Blue Badge?" with GOV.UK suffix

    When I go back in the browser
    Then I can see labelled element "localAuthority" with content "Isle of Anglesey county council"
    When I go back in the browser
    Then I should see the page titled "Choose your local council" with GOV.UK suffix
    When I complete select council page for "england"
    Then I should see the page titled "Your issuing authority" with GOV.UK suffix
    Then I can see labelled element "localAuthority" with content "Blackpool borough council"
    When I complete your local authority page
    Then I should see the page titled "Do you already have a Blue Badge?" with GOV.UK suffix

  Scenario: Find your council - yourself - english - la using third party - find council - change council several times in different ways
    Given I complete applicant page for "yourself"
    Then I should see the page titled "Find your local council" with GOV.UK suffix
    When I complete find council page for "yourself" and la using third party and select a postcode in "england"
    Then I should see page titled "London borough of Southwark uses a different service" with GOV.UK suffix

    When I can click "thatsNotMyLocalCouncilLink" button
    Then I should see the page titled "Choose your local council" with GOV.UK suffix
    When I complete select council page for different service signpost for "scotland"
    Then I should see page titled "Stirling council uses a different service" with GOV.UK suffix

    When I can click "thatsNotMyLocalCouncilLink" button
    Then I should see the page titled "Choose your local council" with GOV.UK suffix
    When I complete select council page for different service signpost for "wales"
    Then I should see page titled "City of Cardiff council uses a different service" with GOV.UK suffix

    When I go back in the browser
    Then I should see the page titled "Choose your local council" with GOV.UK suffix
    When I complete select council page for different service signpost for "england"
    Then I should see page titled "London borough of Southwark uses a different service" with GOV.UK suffix

  Scenario: Find your council - yourself - english - skip find council
    Given I complete applicant page for "yourself"
    Then  I should see the page titled "Find your local council" with GOV.UK suffix
    And   I skip find council page
    Then  I should see the page titled "Choose your local council" with GOV.UK suffix
    And I can click on element "dontKnowCouncil" link
    Then I should see the page titled "Find your local council" with GOV.UK suffix
