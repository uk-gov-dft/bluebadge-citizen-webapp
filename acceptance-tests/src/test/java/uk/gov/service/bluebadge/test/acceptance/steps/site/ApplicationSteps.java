package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.ApplicantPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.ApplicationFixture;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class ApplicationSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private ApplicantPage applicantPage;
  private CommonPage commonPage;
  private ApplicationFixture applicationFixture;
  private String journeyOption;

  @Autowired
  public ApplicationSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }

  @Given("^I navigate to applicant page and validate for \"(yourself|someone else)\"")
  public void iNavigateToApplicantPageAndValidate(String applicant) throws Exception {

    if ("yourself".equals(applicant.toLowerCase())) {
      journeyOption = applicantPage.APPLICANT_TYPE_YOURSELF_OPTION;
    } else {
      journeyOption = applicantPage.APPLICANT_TYPE_SOMELSE_OPTION;
    }

    commonPage.openByPageName("applicant");
    commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(applicantPage.PAGE_TITLE);
    commonSteps.iShouldSeeTheHeading(applicantPage.PAGE_HEADING);
    commonSteps.iClickOnContinueButton();
    commonSteps.andIshouldSeeErrorSummaryBox();
    commonSteps.iShouldSeeTextOnPage(applicantPage.VALIDATION_MESSAGE_FOR_NO_OPTION);
    commonPage.findPageElementById(journeyOption).click();
    commonSteps.iClickOnContinueButton();
  }
}
