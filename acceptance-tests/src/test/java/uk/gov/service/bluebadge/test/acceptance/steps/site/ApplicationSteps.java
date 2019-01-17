package uk.gov.service.bluebadge.test.acceptance.steps.site;

import com.google.common.collect.Lists;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.ApplicantPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class ApplicationSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;

  @Autowired
  public ApplicationSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }

  @Given(
      "^I navigate to applicant page and validate for \"(yourself|someone else|an organisation)\"")
  public void iNavigateToApplicantPageAndValidate(String applicant) throws Exception {
    String journeyOption;

    if ("yourself".equals(applicant.toLowerCase())) {
      journeyOption = ApplicantPage.APPLICANT_TYPE_OPTION_LIST;
    } else if ("someone else".equals(applicant.toLowerCase())) {
      journeyOption = ApplicantPage.APPLICANT_TYPE_SOMELSE_OPTION;
    } else {
      journeyOption = ApplicantPage.APPLICANT_TYPE_ORG_OPTION;
    }

    commonPage.openByPageName("applicant");
    this.verifyPageContent(journeyOption);
  }

  public void verifyPageContent(String journeyOption) {
    commonSteps.iShouldSeeTheCorrectURL(ApplicantPage.PAGE_URL);
    commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(ApplicantPage.PAGE_TITLE);
    commonSteps.iShouldSeeTheHeading(ApplicantPage.PAGE_HEADING);
    commonSteps.iClickOnContinueButton();
    commonSteps.andIshouldSeeErrorSummaryBox();
    commonSteps.iShouldSeeErrorSummaryBoxWithValidationMessagesInOrder(
        Lists.newArrayList(ApplicantPage.VALIDATION_MESSAGE_FOR_NO_OPTION));
    commonSteps.iShouldSeeTextOnPage(ApplicantPage.VALIDATION_MESSAGE_FOR_NO_OPTION);

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
