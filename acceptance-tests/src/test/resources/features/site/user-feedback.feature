@Feedback
Feature: Dft BlueBadge Display User Feedback on all pages

  As a citizen
  If I navigate to the landing page
  I should see user feedback link

  Scenario: Verify I see user feedback link is displaying on the landing page
    Given   I navigate to the "applicant" page
    Then    I can see feedback link

