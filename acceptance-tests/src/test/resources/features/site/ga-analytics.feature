@GAAnalytics
Feature: Dft BlueBadge Google Analytics on all pages

  As a citizen
  If I navigate to the landing page
  It should track my journey in google analytics

  Scenario: Verify Google Analytics tag is present on landing page
    Given   I navigate to the "applicant" page
    Then    The google analytics tag should be available

