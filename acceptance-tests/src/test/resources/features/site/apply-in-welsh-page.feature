@ApplyInWelshPage
Feature: Dft BlueBadge Display Apply in Welsh page

  As a citizen I should be able to navigate to the apply in Welsh page

  Scenario: Verify I can navigate to apply in Welsh
    Given   I navigate to the "apply-in-welsh" page
    Then    I should see page titled "Apply in Welsh" with GOV.UK suffix
    And     I should see the title "Apply in Welsh"
    And     I can see the continue button with link to external welsh site

  Scenario: Verify I can navigate to apply in Welsh page from the footer
    Given   I navigate to the "home" page
    And     I can click on "Welsh"
    Then    I should see page titled "Apply in Welsh" with GOV.UK suffix

