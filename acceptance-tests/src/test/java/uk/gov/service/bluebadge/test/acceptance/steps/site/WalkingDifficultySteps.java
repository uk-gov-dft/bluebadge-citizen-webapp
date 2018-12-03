package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.WalkingDifficultyPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class WalkingDifficultySteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;

  @Autowired
  public WalkingDifficultySteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }

  @And("^I validate walking difficulty page for \"(yourself|someone else)\" for \"(HELP|NONE)\"")
  public void iValidateWalkingDifficultyPageFor(String applicant, String option) {
    verifyPageContent(applicant);
    commonSteps.iVerifyValidationMessage(WalkingDifficultyPage.VALIDATION_MESSAGE_FOR_NO_OPTION);

    enterValidValuesAndContinue(option);
  }

  public void verifyPageContent(String applicant) {

    commonSteps.iShouldSeeTheCorrectURL(WalkingDifficultyPage.PAGE_URL);

    if ("yourself".equals(applicant.toLowerCase())) {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(
          WalkingDifficultyPage.PAGE_TITLE_YOURSELF);
      commonSteps.iShouldSeeTheHeading(WalkingDifficultyPage.PAGE_TITLE_YOURSELF);
    } else {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(
          WalkingDifficultyPage.PAGE_TITLE_SOMEONE_ELSE);
      commonSteps.iShouldSeeTheHeading(WalkingDifficultyPage.PAGE_TITLE_SOMEONE_ELSE);
    }
  }

  @And("I complete what makes walking difficult page for \"(HELP|PLAN|PAIN|DANGEROUS|NONE)\"$")
  public void enterValidValuesAndContinue(String difficulty) {

    if ("HELP".equals(difficulty)) {
      commonPage.selectRadioButton(WalkingDifficultyPage.WALKING_DIFFICULTIES_LIST);
    } else {
      commonPage.selectRadioButton(WalkingDifficultyPage.WALKING_DIFFICULTIES_LIST + "." + difficulty);
    }

    commonSteps.iClickOnContinueButton();
  }
}
