package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.NotPaidPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class NotPaidSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;

  @Autowired
  public NotPaidSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }

  @And("I complete not paid page choosing enter payment details")
  public void iCompleteNotPaidPageChoosingEnterPaymentDetails() {
    commonPage.findElementWithUiPath(NotPaidPage.ENTER_PAYMENT_DETAILS).click();
    commonPage.findElementWithUiPath(NotPaidPage.CONTINUE_BUTTON).click();
  }

  @And("I complete not paid page choosing submit application and pay later")
  public void iCompleteNotPaidPageChoosingSubmitApplicationAndPayLater() {
    commonPage.findElementWithUiPath(NotPaidPage.SUBMIT_APPLICATION_AND_PAY_LATER).click();
    commonPage.findElementWithUiPath(NotPaidPage.CONTINUE_BUTTON).click();
  }
}
