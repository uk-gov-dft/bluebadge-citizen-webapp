package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.ConfirmYourPaymentsPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class ConfirmYourPaymentSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;

  @Autowired
  public ConfirmYourPaymentSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }

  @And("^I confirm payment in confirm payment page$")
  public void iConfirmPaymentInConfirmPaymentPage() {
    commonPage.findPageElementById(ConfirmYourPaymentsPage.CONFIRM_BUTTON_ID).click();
  }

  @And("^I cancel payment in confirm payment page$")
  public void iCancelPaymentInConfirmPaymentPage() {
    commonPage.findPageElementById(ConfirmYourPaymentsPage.CANCEL_PAYMENT_LINK_ID).click();
  }
}
