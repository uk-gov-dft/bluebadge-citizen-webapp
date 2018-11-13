package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.NinoPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class NINOSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;

  @Autowired
  public NINOSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }

  @And("^I validate nino page for a \"([^\"]*)\" application$")
  public void iValidateNinoPageForAApplication(String applicant) {
    verifyPageContent(applicant);

    //To validate Empty NINO
    commonSteps.iVerifyValidationMessage(NinoPage.VALIDATION_MESSAGE_FOR_EMPTY_NINO);

    //To validate invalid NINO
    commonPage.findPageElementById("nino").sendKeys(NinoPage.INVALID_NINO);
    commonSteps.iVerifyValidationMessage(NinoPage.VALIDATION_MESSAGE_FOR_INVALID_NINO);

    //Enter valid values and move on
    commonPage.findPageElementById("nino").clear();
    commonPage.findPageElementById("nino").sendKeys(NinoPage.VALID_NINO);

    commonSteps.iClickOnContinueButton();
  }

  public void verifyPageContent(String applicant) {

    commonSteps.iShouldSeeTheCorrectURL(NinoPage.PAGE_URL);

    if ("you".equals(applicant.toLowerCase()) | "self".equals(applicant.toLowerCase())) {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(NinoPage.PAGE_TITLE_YOURSELF);
      commonSteps.iShouldSeeTheHeading(NinoPage.PAGE_TITLE_YOURSELF);
    } else {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(NinoPage.PAGE_TITLE_SOMEONE_ELSE);
      commonSteps.iShouldSeeTheHeading(NinoPage.PAGE_TITLE_SOMEONE_ELSE);
    }
  }
}
