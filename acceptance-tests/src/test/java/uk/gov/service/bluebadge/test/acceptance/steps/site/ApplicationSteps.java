package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.ApplicantPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.ApplicationFixture;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

import java.time.LocalDate;

public class ApplicationSteps extends AbstractSpringSteps {

    private CommonSteps commonSteps;
    private ApplicantPage applicantPage;
    private CommonPage commonPage;
    private ApplicationFixture applicationFixture;

    @Autowired
    public ApplicationSteps(CommonPage commonPage, CommonSteps commonSteps) {
        this.commonPage = commonPage;
        this.commonSteps = commonSteps;
    }

    @Given("^I navigate to applicant page and validate for \"(yourself|someone else|an organisation)\"")
    public void iNavigateToApplicantPageAndValidate(String applicant) throws Exception {
        String journeyOption;

        if ("yourself".equals(applicant.toLowerCase())) {
            journeyOption = applicantPage.APPLICANT_TYPE_OPTION_LIST;
        } else if ("someone else".equals(applicant.toLowerCase())) {
            journeyOption = applicantPage.APPLICANT_TYPE_SOMELSE_OPTION;
        } else {
            journeyOption = applicantPage.APPLICANT_TYPE_ORG_OPTION;
        }

        commonPage.openByPageName("applicant");
        this.verifyPageContent(journeyOption);
    }


    public void verifyPageContent(String journeyOption) {
        commonSteps.iShouldSeeTheCorrectURL(applicantPage.PAGE_URL);
        commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(applicantPage.PAGE_TITLE);
        commonSteps.iShouldSeeTheHeading(applicantPage.PAGE_HEADING);
        commonSteps.iClickOnContinueButton();
        commonSteps.andIshouldSeeErrorSummaryBox();
        commonSteps.iShouldSeeTextOnPage(applicantPage.VALIDATION_MESSAGE_FOR_NO_OPTION);

        commonPage.selectRadioButton(journeyOption);
        commonSteps.iClickOnContinueButton();
    }

  @And("^I complete prove benefit page for \"(yes|no)\"")
  public void iCompleteProveBenefitPage(String opt) {
    if (opt.equals("yes")) {
      commonPage.findPageElementById("hasProof").click();
    } else {
      LocalDate date = LocalDate.now();
      String day = Integer.toString(date.getDayOfMonth());
      String month = Integer.toString(date.getMonth().getValue());
      String year = Integer.toString(date.getYear() + 1);

      commonPage.findPageElementById("hasProof.no").click();
      commonPage.findPageElementById("awardEndDate").sendKeys(day);
      commonPage.findPageElementById("awardEndDate.month").sendKeys(month);
      commonPage.findPageElementById("awardEndDate.year").sendKeys(year);
    }

    commonSteps.iClickOnContinueButton();
  }
}
