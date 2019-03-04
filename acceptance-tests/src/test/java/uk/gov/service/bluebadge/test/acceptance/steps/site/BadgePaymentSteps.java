package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.BadgePaymentPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class BadgePaymentSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;

  @Autowired
  public BadgePaymentSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }

  @And("I complete badge payment page with pay online now")
  public void iCompleteBadgePaymentPageWithPayOnlineNow() {
    commonPage.findElementWithUiPath(BadgePaymentPage.PAY_ONLINE_NOW).click();
  }

  @And("I complete badge payment page with submit application and pay later")
  public void iCompleteBadgePaymentPageWithSubmitApplicationAndPayLater() {
    commonPage.findElementWithUiPath(BadgePaymentPage.SUBMIT_APPLICATION_AND_PAY_LATER).click();
  }
}
