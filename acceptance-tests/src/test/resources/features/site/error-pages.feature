@ErrorPages
Feature: Dft BlueBadge Display Error Pages

  As a citizen
  If I navigate to the wrong url or if the application has a problem
  I should see an appropriate error page displayed

  Scenario: Verify I see 404 page not found when navigate to url that doesn't exist
    Given   I navigate to the "page-that-does-not-exist" page
    Then    I should see the page titled "Page not found - GOV.UK Apply for a Blue Badge"
