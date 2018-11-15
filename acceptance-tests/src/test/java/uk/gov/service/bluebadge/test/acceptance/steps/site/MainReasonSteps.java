package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.MainReasonPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class MainReasonSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;

  @Autowired
  public MainReasonSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }

  @And("^I validate main reason page for \"(yourself|someone else)\" for \"(WALKD|NONE)\"")
  public void iValidateMainReasonPageFor(String applicant, String option) {
    verifyPageContent(applicant);
    commonSteps.iVerifyValidationMessage(MainReasonPage.VALIDATION_MESSAGE_FOR_NO_OPTION);
    commonPage.findPageElementById(MainReasonPage.MAIN_REASONS_LIST + "." + option).click();
    commonSteps.iClickOnContinueButton();
  }

  public void verifyPageContent(String applicant) {

    commonSteps.iShouldSeeTheCorrectURL(MainReasonPage.PAGE_URL);

    if ("yourself".equals(applicant.toLowerCase())) {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(MainReasonPage.PAGE_TITLE_YOURSELF);
      commonSteps.iShouldSeeTheHeading(MainReasonPage.PAGE_TITLE_YOURSELF);
    } else {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(MainReasonPage.PAGE_TITLE_SOMEONE_ELSE);
      commonSteps.iShouldSeeTheHeading(MainReasonPage.PAGE_TITLE_SOMEONE_ELSE);
    }
  }
}
