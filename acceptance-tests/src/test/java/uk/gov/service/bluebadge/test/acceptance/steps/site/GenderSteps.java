package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.GenderPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class GenderSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;

  @Autowired
  public GenderSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }

  @And("^I validate gender page for a \"([^\"]*)\" application with option as \"([^\"]*)\"$")
  public void iValidateGenderPageForAApplication(String applicant, String option) {
    verifyPageContent(applicant);

    //To validate Non Selection of Gender
    commonSteps.iVerifyValidationMessage(GenderPage.VALIDATION_MESSAGE_FOR_NON_SELECTION_OF_GENDER);

    commonPage.findElementWithUiPath(GenderPage.GENDER_LIST + "." + option).click();
    commonSteps.iClickOnContinueButton();
  }

  public void verifyPageContent(String applicant) {

    commonSteps.iShouldSeeTheCorrectURL(GenderPage.PAGE_URL);

    if ("you".equals(applicant.toLowerCase()) | "self".equals(applicant.toLowerCase())) {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(GenderPage.PAGE_TITLE_YOURSELF);
      commonSteps.iShouldSeeTheHeading(GenderPage.PAGE_TITLE_YOURSELF);
    } else {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(GenderPage.PAGE_TITLE_SOMEONE_ELSE);
      commonSteps.iShouldSeeTheHeading(GenderPage.PAGE_TITLE_SOMEONE_ELSE);
    }
  }
}
