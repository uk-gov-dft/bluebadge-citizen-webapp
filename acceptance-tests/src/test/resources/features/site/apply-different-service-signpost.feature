@ApplyDifferentServiceSignpost
Feature: Dft BlueBadge Display Different service signpost page

  As a user
  I want to be able to select my local council
  So that I can apply for a Blue Badge

  Scenario: Display different service signpost page
    Given I complete applicant page for "yourself"
    And I complete select council page for different service signpost
    Then I should see page titled "Different service signpost" with GOV.UK suffix
