package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.BenifitsPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class BenefitsSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;

  @Autowired
  public BenefitsSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }

  @And(
      "^I validate benefit page for \"(yourself|someone else)\" for \"(PIP|DLA|AFRFCS|WPMS|NONE)\"")
  public void iValidateBenefitPageFor(String applicant, String option) {
    verifyPageContent(applicant);
    commonSteps.iVerifyValidationMessage(BenifitsPage.VALIDATION_MESSAGE_FOR_NO_OPTION);

    if ("PIP".equals(option)) {
      commonPage.selectRadioButton(BenifitsPage.BENEFIT_RECEIVED_LIST);
    } else {
      commonPage.selectRadioButton(BenifitsPage.BENEFIT_RECEIVED_LIST + "." + option);
    }
    commonSteps.iClickOnContinueButton();
  }

  public void verifyPageContent(String applicant) {
    commonSteps.iShouldSeeTheCorrectURL(BenifitsPage.PAGE_URL);
    if ("yourself".equals(applicant.toLowerCase())) {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(BenifitsPage.PAGE_TITLE_YOURSELF);
      commonSteps.iShouldSeeTheHeading(BenifitsPage.PAGE_TITLE_YOURSELF);
    } else {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(BenifitsPage.PAGE_TITLE_SOMEONE_ELSE);
      commonSteps.iShouldSeeTheHeading(BenifitsPage.PAGE_TITLE_SOMEONE_ELSE);
    }
  }
}
