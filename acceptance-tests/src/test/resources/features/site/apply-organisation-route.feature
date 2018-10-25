@ApplyOrganisationRoute
Feature: DFT Blue badge Citizen app new application - Organisation
  As a representative of an organisation I want to be able to get information how to apply for a blue badge


  Scenario: Apply for organisation which cares and transports disabled
    Given I complete applicant page for "organisation"
    And   I complete select council page for "england"
    And   I complete your local authority page
    
    And   I complete does organisation care for "YES"
    And   I complete does organisation transport for "YES"
    Then  I should see .* page titled "Your organisation may be eligible for a Blue Badge" with GOV.UK suffix
    
  Scenario: Apply for organisation which cares for disabled but doesn't transports them
    Given I complete applicant page for "organisation"
    And   I complete select council page for "england"
    And   I complete your local authority page
    
    And   I complete does organisation care for "YES"
    And   I complete does organisation transport for "NO"
    Then  I should see .* page titled "Your organisation is not eligible for a Blue Badge" with GOV.UK suffix
    
  Scenario: Apply for organisation which doesn't cares for disabled 
    Given I complete applicant page for "organisation"
    And   I complete select council page for "england"
    And   I complete your local authority page
    
    And   I complete does organisation care for "NO"
    Then  I should see .* page titled "Your organisation is not eligible for a Blue Badge" with GOV.UK suffix
    