@SubmitApplicationPIPRoutePaymentsEnable @CreateLocalCouncilAndAuthorityWithPaymentsEnable
Feature: DFT Blue badge Citizen app new application - PIP
  As a citizen
  I want to pay for my Blue Badge application
  So that I can get a Blue Badge

  Scenario: Create a successful new application for myself with payments enable and submit application and pay later
    Given I complete applicant page for "yourself"
    And   I complete select council page with payments enable for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "PIP"
    And   I complete moving around points page for "12"
    Then  I should see "You're" eligible page
    When  I complete eligible page
    And   I complete what's your name page
    And   I complete date of birth page for "ADULT"
    And   I complete gender page for "Man"
    And   I complete NI number page
    And   I complete address page
    And   I complete contact page for "yourself"
    And   I complete prove benefit page for "yes"
    And   I complete "upload benefit" page with a "GIF" document
    And   I complete "prove ID" page with a "GIF" document
    And   I complete "provide photo" page with a "GIF" document
    And   I complete "prove address" page with a "GIF" document
    And   I complete declaration page
    Then  I should see page titled "Pay for the Blue Badge" with GOV.UK suffix
    And   I complete badge payment page with submit application and pay later
    Then  I should see page titled "Application submitted" with GOV.UK suffix
    And   I cannot see labelled element "p.paymentSuccessfulText"

  Scenario: Create a successful new application for myself with payments enable and pay now
    Given I complete applicant page for "yourself"
    And   I complete select council page with payments enable for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "PIP"
    And   I complete moving around points page for "12"
    Then  I should see "You're" eligible page
    When  I complete eligible page
    And   I complete what's your name page
    And   I complete date of birth page for "ADULT"
    And   I complete gender page for "Man"
    And   I complete NI number page
    And   I complete address page
    And   I complete contact page for "yourself"
    And   I complete prove benefit page for "yes"
    And   I complete "upload benefit" page with a "GIF" document
    And   I complete "prove ID" page with a "GIF" document
    And   I complete "provide photo" page with a "GIF" document
    And   I complete "prove address" page with a "GIF" document
    And   I complete declaration page
    Then  I should see page titled "Pay for the Blue Badge" with GOV.UK suffix
    And   I complete badge payment page with pay online now
    Then  I should see page titled "Enter card details"
    And   I should see payment description "Blue Badge issue fee"
    And   I should see badge cost "£40.00"
    And   I complete enter cards details page and press continue
    And   I should see page titled "Confirm your payment"
    And   I should see payment description "Blue Badge issue fee"
    And   I should see badge cost "£40.00"
    When  I confirm payment in confirm payment page
    Then  I should see page titled "Application submitted" with GOV.UK suffix
    And   I see labelled element "p.paymentSuccessfulText" with content "You've paid the £40 issue fee."

  Scenario: Create a successful new application for myself with payments enable and pay now - payment in welsh
    Given I complete applicant page for "yourself"
    And   I complete select council page with payments enable for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "PIP"
    And   I complete moving around points page for "12"
    Then  I should see "You're" eligible page
    When  I complete eligible page
    And   I complete what's your name page
    And   I complete date of birth page for "ADULT"
    And   I complete gender page for "Man"
    And   I complete NI number page
    And   I complete address page
    And   I complete contact page for "yourself"
    And   I complete prove benefit page for "yes"
    And   I complete "upload benefit" page with a "GIF" document
    And   I complete "prove ID" page with a "GIF" document
    And   I complete "provide photo" page with a "GIF" document
    And   I complete "prove address" page with a "GIF" document
    And   I complete declaration page
    Then  I should see page titled "Pay for the Blue Badge" with GOV.UK suffix
    And   I change language
    Then  I should see page titled in Welsh "Talu am y Bathodyn Glas" with GOV.UK suffix
    And   I complete badge payment page with pay online now
    Then  I should see page titled "Rhowch fanylion y cerdyn"
    And   I should see payment description "Ffi am Bathodyn Glas"
    And   I should see badge cost "£40.00"
    And   I complete enter cards details page and press continue
    And   I should see page titled "Cadarnhau eich taliad"
    And   I should see payment description "Ffi am Bathodyn Glas"
    And   I should see badge cost "£40.00"
    When  I confirm payment in confirm payment page
    Then  I should see page titled in Welsh "Cais wedi ei gyflwyno" with GOV.UK suffix
    And   I see labelled element "p.paymentSuccessfulText" with content "Rydych wedi talu'r ffi o £40."

  Scenario: Create a successful new application for myself with payments enable and pay now with cancellation of payment twice before paying successfully
    Given I complete applicant page for "yourself"
    And   I complete select council page with payments enable for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "PIP"
    And   I complete moving around points page for "12"
    Then  I should see "You're" eligible page
    When  I complete eligible page
    And   I complete what's your name page
    And   I complete date of birth page for "ADULT"
    And   I complete gender page for "Man"
    And   I complete NI number page
    And   I complete address page
    And   I complete contact page for "yourself"
    And   I complete prove benefit page for "yes"
    And   I complete "upload benefit" page with a "GIF" document
    And   I complete "prove ID" page with a "GIF" document
    And   I complete "provide photo" page with a "GIF" document
    And   I complete "prove address" page with a "GIF" document
    And   I complete declaration page
    Then  I should see page titled "Pay for the Blue Badge" with GOV.UK suffix
    And   I complete badge payment page with pay online now
    Then  I should see page titled "Enter card details"
    And   I should see payment description "Blue Badge issue fee"
    And   I should see badge cost "£40.00"
    And   I don't complete enter cards details page and press cancel payment
    Then  I should see page titled "Your payment has been cancelled - GOV.UK Pay"
    When  I click on button with id "return-url"
    Then  I should see page titled "Pay for the Blue Badge" with GOV.UK suffix
    And   I complete badge payment page with pay online now
    Then  I should see page titled "Enter card details"
    And   I should see payment description "Blue Badge issue fee"
    And   I should see badge cost "£40.00"
    And   I complete enter cards details page and press continue
    And   I should see page titled "Confirm your payment"
    And   I should see payment description "Blue Badge issue fee"
    And   I should see badge cost "£40.00"
    And   I cancel payment in confirm payment page
    Then  I should see page titled "Your payment has been cancelled - GOV.UK Pay"
    When  I click on button with id "return-url"
    Then  I should see page titled "Pay for the Blue Badge" with GOV.UK suffix
    And   I complete badge payment page with pay online now
    Then  I should see page titled "Enter card details"
    And   I should see payment description "Blue Badge issue fee"
    And   I should see badge cost "£40.00"
    And   I complete enter cards details page and press continue
    And   I should see page titled "Confirm your payment"
    And   I should see payment description "Blue Badge issue fee"
    And   I should see badge cost "£40.00"
    When  I confirm payment in confirm payment page
    Then  I should see page titled "Application submitted" with GOV.UK suffix
    And   I see labelled element "p.paymentSuccessfulText" with content "You've paid the £40 issue fee."
