@ApplyDifferentServiceSignpost
Feature: Dft BlueBadge Display Different service signpost page

  As a user
  I want to be able to select my local council
  So that I can apply for a Blue Badge

  Scenario: Display different service signpost page
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And I complete select council page for different service signpost for "england"
    Then I should see page titled "London borough of Southwark uses a different service" with GOV.UK suffix

    And I go back in the browser
    Then I should see page titled "Choose your local council" with GOV.UK suffix
    And I complete select council page for different service signpost for "england"
    Then I should see page titled "London borough of Southwark uses a different service" with GOV.UK suffix

    When I can click on element "continue" link
    And I go back in the browser
    Then I should see page titled "Who are you applying for?" with GOV.UK suffix
