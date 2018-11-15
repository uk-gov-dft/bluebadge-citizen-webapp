package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class RegisteredCouncilSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;

  @Autowired
  public RegisteredCouncilSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }

  @And("^I complete registered page for \"(yes|no)\"$")
  public void iCompleteRegisteredPage(String answer) throws Throwable {
    commonPage.findElementWithUiPath("hasRegistered.option." + answer).click();
    commonSteps.iClickOnContinueButton();
  }

  @And("^I complete permission page for \"(yes|no)\"$")
  public void iCompletePermissionPage(String answer) throws Throwable {
    commonPage.findElementWithUiPath("hasPermission.option." + answer).click();
    commonSteps.iClickOnContinueButton();
  }

  @And("^I complete registered council page$")
  public void iCompleteRegisteredCouncilPage() throws Throwable {
    commonPage.findPageElementById("registeredCouncil").sendKeys("a");
    commonSteps.iClickOnContinueButton();
  }
}
