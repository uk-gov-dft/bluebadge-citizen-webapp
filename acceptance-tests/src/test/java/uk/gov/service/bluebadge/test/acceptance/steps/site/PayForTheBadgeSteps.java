package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.PayForTheBadgePage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class PayForTheBadgeSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;

  @Autowired
  public PayForTheBadgeSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }

  @And("I complete pay for blue badge page with pay online now")
  public void iCompletePayForBlueBadgePageWithPayOnlineNow() {
    commonPage.findElementWithUiPath(PayForTheBadgePage.PAY_ONLINE_NOW).click();
  }

  @And("I complete pay for blue badge page with submit application and pay later")
  public void iCompletePayForBlueBadgePageWithSubmitApplicationAndPayLater() {
    commonPage.findElementWithUiPath(PayForTheBadgePage.SUBMIT_APPLICATION_AND_PAY_LATER).click();
  }
}
