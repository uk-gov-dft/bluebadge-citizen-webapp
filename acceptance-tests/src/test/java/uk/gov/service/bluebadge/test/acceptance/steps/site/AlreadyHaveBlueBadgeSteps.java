package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.AlreadyHaveBlueBadgePage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class AlreadyHaveBlueBadgeSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;

  @Autowired
  public AlreadyHaveBlueBadgeSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }

  @And(
      "^I validate already have a blue badge page for \"(yourself|someone else)\" for \"(Yes|No)\"")
  public void iValidateAlreadyHaveABlueBadgePageFor(String applicant, String option)
      throws Throwable {
    verifyPageContent(applicant);
    commonSteps.iVerifyValidationMessage(AlreadyHaveBlueBadgePage.VALIDATION_MESSAGE_FOR_NO_OPTION);

    if ("Yes".equals(option)) {
      commonPage.selectRadioButton(AlreadyHaveBlueBadgePage.EXISTING_BADGE_OPTION);
      commonSteps.iVerifyValidationMessage(
          AlreadyHaveBlueBadgePage.VALIDATION_MESSAGE_FOR_NO_BADGE);
      commonPage
          .findPageElementById(AlreadyHaveBlueBadgePage.BADGE_NUMBER)
          .sendKeys("AbEddd 0 0882X1217R");
    } else
      commonPage.selectRadioButton(
          AlreadyHaveBlueBadgePage.EXISTING_BADGE_OPTION + "_" + option.toLowerCase());

    commonSteps.iClickOnContinueButton();
  }

  public void verifyPageContent(String applicant) {
    commonSteps.iShouldSeeTheCorrectURL(AlreadyHaveBlueBadgePage.PAGE_URL);
    if ("yourself".equals(applicant.toLowerCase())) {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(
          AlreadyHaveBlueBadgePage.PAGE_TITLE_YOURSELF);
      commonSteps.iShouldSeeTheHeading(AlreadyHaveBlueBadgePage.PAGE_TITLE_YOURSELF);
    } else {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(
          AlreadyHaveBlueBadgePage.PAGE_TITLE_SOMEONE_ELSE);
      commonSteps.iShouldSeeTheHeading(AlreadyHaveBlueBadgePage.PAGE_TITLE_SOMEONE_ELSE);
    }
  }
}
