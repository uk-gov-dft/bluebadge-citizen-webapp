package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.ReturnToApplicationPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class ReturnToApplicationSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;

  @Autowired
  public ReturnToApplicationSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }

  public void verifyPageContent(String applicant) {

    commonSteps.iShouldSeeTheCorrectURL(ReturnToApplicationPage.PAGE_URL);

    if ("yourself".equals(applicant.toLowerCase())) {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(ReturnToApplicationPage.PAGE_TITLE_YOURSELF);
      commonSteps.iShouldSeeTheHeading(ReturnToApplicationPage.PAGE_TITLE_YOURSELF);
    } else {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(ReturnToApplicationPage.PAGE_TITLE_SOMEONE_ELSE);
      commonSteps.iShouldSeeTheHeading(ReturnToApplicationPage.PAGE_TITLE_SOMEONE_ELSE);
    }
  }


  @And("^I validate return to saved application page for a \"([^\"]*)\" application$")
  public void iValidateReturnToSavedApplicationPageForAApplication(String applicant) {
    verifyPageContent(applicant);

    //To validate Empty EMAIL
    commonSteps.iVerifyValidationMessage(ReturnToApplicationPage.VALIDATION_MESSAGE_FOR_EMPTY_EMAIL);

    //Enter valid values and move on
    commonPage.findElementWithUiPath(ReturnToApplicationPage.EMAIL).clear();
    commonPage.findElementWithUiPath(ReturnToApplicationPage.EMAIL).sendKeys(ReturnToApplicationPage.VALID_EMAIL);

    commonSteps.iClickOnContinueButton();
  }
}
