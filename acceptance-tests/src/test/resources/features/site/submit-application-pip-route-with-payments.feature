@SubmitApplicationPIPRoutePaymentsEnable @CreateLocalCouncilAndAuthorityWithPaymentsEnable
Feature: DFT Blue badge Citizen app new application - PIP
  As a citizen
  I want to pay for my Blue Badge application
  So that I can get a Blue Badge

  Scenario: Create a successful new application for myself with payments enable and submit application and pay later
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page with payments enable for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "PIP"
    And   I complete moving around points page for "12"
    Then  I should see "You're" eligible page
    When  I complete eligible page

    Then  I see the "PIP" task list page
    And   I can click on the "Enter personal details" link
    And   I complete what's your name page
    And   I complete date of birth page for "ADULT"
    And   I complete gender page for "Man"
    And   I complete NI number page
    And   I complete address page
    And   I complete contact page for "yourself"

    Then  I see the "PIP" task list page as Adult
    And   I see task "Enter personal details" as COMPLETED
    And   I can click on the "Provide proof of benefit" link
    And   I complete prove benefit page for "yes"
    And   I complete "upload benefit" page with a "GIF" document

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Prove identity" link
    And   I complete "prove ID" page with a "GIF" document

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Prove address" link
    And   I complete "prove address" page with a "GIF" document

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Add a photo for the badge" link
    And   I complete "provide photo" page with a "GIF" document

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Agree to declaration" link
    And   I complete declaration page

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Submit and pay" link
    Then  I should see page titled "Pay for the Blue Badge" with GOV.UK suffix
    And   I complete badge payment page with submit application and pay later
    Then  I should see page titled "Application submitted" with GOV.UK suffix
    And   I see labelled element "p.paymentsEnabledButPaymentNotSuccessful" with content "Because you've chosen to pay later, Birmingham city council will contact you regarding payment."
    And   I cannot see labelled element "p.paymentsNotEnabled"
    And   I cannot see labelled element "p.paymentSuccessfulText"

  Scenario: Create a successful new application for myself with payments enable and pay now
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page with payments enable for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "PIP"
    And   I complete moving around points page for "12"
    Then  I should see "You're" eligible page
    When  I complete eligible page

    Then  I see the "PIP" task list page
    And   I can click on the "Enter personal details" link
    And   I complete what's your name page
    And   I complete date of birth page for "ADULT"
    And   I complete gender page for "Man"
    And   I complete NI number page
    And   I complete address page
    And   I complete contact page for "yourself"

    Then  I see the "PIP" task list page as Adult
    And   I see task "Enter personal details" as COMPLETED
    And   I can click on the "Provide proof of benefit" link
    And   I complete prove benefit page for "yes"
    And   I complete "upload benefit" page with a "GIF" document

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Prove identity" link
    And   I complete "prove ID" page with a "GIF" document

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Prove address" link
    And   I complete "prove address" page with a "GIF" document

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Add a photo for the badge" link
    And   I complete "provide photo" page with a "GIF" document

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Agree to declaration" link
    And   I complete declaration page

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Submit and pay" link
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
    And   I cannot see labelled element "p.paymentsEnabledButPaymentNotSuccessful"
    And   I cannot see labelled element "p.paymentsNotEnabled"
    And   I see labelled element "p.paymentSuccessfulText" with content "You've paid the £40 issue fee."

  Scenario: Create a successful new application for myself with payments enable and pay now - payment in welsh
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page with payments enable for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "PIP"
    And   I complete moving around points page for "12"
    Then  I should see "You're" eligible page
    When  I complete eligible page

    Then  I see the "PIP" task list page
    And   I can click on the "Enter personal details" link
    And   I complete what's your name page
    And   I complete date of birth page for "ADULT"
    And   I complete gender page for "Man"
    And   I complete NI number page
    And   I complete address page
    And   I complete contact page for "yourself"

    Then  I see the "PIP" task list page as Adult
    And   I see task "Enter personal details" as COMPLETED
    And   I can click on the "Provide proof of benefit" link
    And   I complete prove benefit page for "yes"
    And   I complete "upload benefit" page with a "GIF" document

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Prove identity" link
    And   I complete "prove ID" page with a "GIF" document

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Prove address" link
    And   I complete "prove address" page with a "GIF" document

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Add a photo for the badge" link
    And   I complete "provide photo" page with a "GIF" document

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Agree to declaration" link
    And   I complete declaration page

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Submit and pay" link
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
    And   I cannot see labelled element "p.paymentsEnabledButPaymentNotSuccessful"
    And   I cannot see labelled element "p.paymentsNotEnabled"

  Scenario: Create a successful new application for myself with payments enable and pay later with cancellation of payment and then in not paid page decide to submit later
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page with payments enable for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "PIP"
    And   I complete moving around points page for "12"
    When  I complete eligible page

    Then  I see the "PIP" task list page
    And   I can click on the "Enter personal details" link
    And   I complete what's your name page
    And   I complete date of birth page for "ADULT"
    And   I complete gender page for "Man"
    And   I complete NI number page
    And   I complete address page
    And   I complete contact page for "yourself"

    Then  I see the "PIP" task list page as Adult
    And   I see task "Enter personal details" as COMPLETED
    And   I can click on the "Provide proof of benefit" link
    And   I complete prove benefit page for "yes"
    And   I complete "upload benefit" page with a "GIF" document

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Prove identity" link
    And   I complete "prove ID" page with a "GIF" document

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Prove address" link
    And   I complete "prove address" page with a "GIF" document

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Add a photo for the badge" link
    And   I complete "provide photo" page with a "GIF" document

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Agree to declaration" link
    And   I complete declaration page

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Submit and pay" link
    Then  I should see page titled "Pay for the Blue Badge" with GOV.UK suffix
    And   I complete badge payment page with pay online now
    Then  I should see page titled "Enter card details"
    And   I should see payment description "Blue Badge issue fee"
    And   I should see badge cost "£40.00"
    And   I don't complete enter cards details page and press cancel payment
    Then  I should see page titled "Your payment has been cancelled - GOV.UK Pay"
    When  I click on button with id "return-url"
    Then  I should see page titled "You have not paid for the badge, do you want to try again?" with GOV.UK suffix
    And   I change language
    Then  I should see page titled in Welsh "Nid ydych wedi talu am y bathodyn. Hoffech chi drïo eto?" with GOV.UK suffix
    And   I see labelled element "retry.label.yes" with content "Hoffwn, nodwch fanylion y taliad"
    And   I see labelled element "retry.label.no" with content "Na hoffwn, cyflwynwch eich cais a thalu wedyn"
    And   I change language
    Then  I should see page titled "You have not paid for the badge, do you want to try again?" with GOV.UK suffix
    And   I complete not paid page choosing enter payment details
    Then  I should see page titled "Enter card details"
    And   I should see payment description "Blue Badge issue fee"
    And   I should see badge cost "£40.00"
    And   I cancel payment in confirm payment page
    Then  I should see page titled "Your payment has been cancelled - GOV.UK Pay"
    When  I click on button with id "return-url"
    Then  I should see page titled "You have not paid for the badge, do you want to try again?" with GOV.UK suffix
    And   I complete not paid page choosing submit application and pay later
    Then  I should see page titled "Application submitted" with GOV.UK suffix
    And   I see labelled element "p.paymentsEnabledButPaymentNotSuccessful" with content "Because you've chosen to pay later, Birmingham city council will contact you regarding payment."
    And   I cannot see labelled element "p.paymentsNotEnabled"
    And   I cannot see labelled element "p.paymentSuccessfulText"


  Scenario: Create a successful new application for myself with payments enable and pay now with cancellation of payment twice before paying successfully
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page with payments enable for "england"
    And   I complete your local authority page
    And   I complete the already have a blue badge page for "YES"
    And   I complete receive benefit page for "PIP"
    And   I complete moving around points page for "12"
    When  I complete eligible page

    Then  I see the "PIP" task list page
    And   I can click on the "Enter personal details" link
    And   I complete what's your name page
    And   I complete date of birth page for "ADULT"
    And   I complete gender page for "Man"
    And   I complete NI number page
    And   I complete address page
    And   I complete contact page for "yourself"

    Then  I see the "PIP" task list page as Adult
    And   I see task "Enter personal details" as COMPLETED
    And   I can click on the "Provide proof of benefit" link
    And   I complete prove benefit page for "yes"
    And   I complete "upload benefit" page with a "GIF" document

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Prove identity" link
    And   I complete "prove ID" page with a "GIF" document

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Prove address" link
    And   I complete "prove address" page with a "GIF" document

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Add a photo for the badge" link
    And   I complete "provide photo" page with a "GIF" document

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Agree to declaration" link
    And   I complete declaration page

    Then  I see the "PIP" task list page as Adult
    And   I can click on the "Submit and pay" link
    Then  I should see page titled "Pay for the Blue Badge" with GOV.UK suffix
    And   I complete badge payment page with pay online now
    Then  I should see page titled "Enter card details"
    And   I should see payment description "Blue Badge issue fee"
    And   I should see badge cost "£40.00"
    And   I don't complete enter cards details page and press cancel payment
    Then  I should see page titled "Your payment has been cancelled - GOV.UK Pay"
    When  I click on button with id "return-url"
    Then  I should see page titled "You have not paid for the badge, do you want to try again?" with GOV.UK suffix
    And   I complete not paid page choosing enter payment details
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
    Then  I should see page titled "You have not paid for the badge, do you want to try again?" with GOV.UK suffix
    And   I complete not paid page choosing enter payment details
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
    And   I cannot see labelled element "p.paymentsEnabledButPaymentNotSuccessful"
    And   I cannot see labelled element "p.paymentsNotEnabled"

  Scenario: Create a successful new application for myself with payments not enabled - England - yourself - english
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page for "england"
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
    Then  I should see page titled "Application submitted" with GOV.UK suffix
    And   I cannot see labelled element "p.paymentSuccessfulText"
    And   I cannot see labelled element "p.paymentsEnabledButPaymentNotSuccessful"
    And   I see labelled element "p.paymentsNotEnabled" with content "If Blackpool borough council charges a fee for the badge, they'll contact you for payment. The fee will be returned if the application is not successful."

  Scenario: Create a successful new application for myself with payments not enabled - England - yourself - welsh
    Given I complete applicant page for "yourself"
    And   I skip find council page
    And   I complete select council page for "england"
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
    And   I change language
    And   I complete declaration page
    Then  I should see page titled in Welsh "Cais wedi ei gyflwyno" with GOV.UK suffix
    And   I cannot see labelled element "p.paymentSuccessfulText"
    And   I cannot see labelled element "p.paymentsEnabledButPaymentNotSuccessful"
    And   I see labelled element "p.paymentsNotEnabled" with content "Os yw Blackpool borough council yn codi tâl am y bathodyn, bydd yn cysylltu â chi am dâl. Cewch yr arian nôl os na fydd eich cais yn llwyddiannus."
