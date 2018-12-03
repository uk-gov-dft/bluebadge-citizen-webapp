package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.HealthConditionsPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.WalkingDifficultyPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.WhatMakesWalkingDifficultiesPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

import java.util.ArrayList;
import java.util.List;

public class WhatMakesWalkingDifficultSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;

  @Autowired
  public WhatMakesWalkingDifficultSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }

  @And("^I validate what makes walking difficult page for a \"(yourself|someone else)\" application for \"(HELP|PLAN|PAIN|DANGEROUS|NONE)\"$")
  public void iValidateWhatMakesWalkingDifficultPageForAApplication(String applicant, String difficulty){
    verifyPageContent(applicant);
    validateMandatoryFields();

    enterValidValuesAndContinue(difficulty);

  }


  public void verifyPageContent(String applicant) {

    commonSteps.iShouldSeeTheCorrectURL(WalkingDifficultyPage.PAGE_URL);

    if ("yourself".equals(applicant.toLowerCase())) {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(WalkingDifficultyPage.PAGE_TITLE_YOURSELF);
      commonSteps.iShouldSeeTheHeading(WalkingDifficultyPage.PAGE_TITLE_YOURSELF);
    } else {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(WalkingDifficultyPage.PAGE_TITLE_SOMEONE_ELSE);
      commonSteps.iShouldSeeTheHeading(WalkingDifficultyPage.PAGE_TITLE_SOMEONE_ELSE);
    }
  }

  private void validateMandatoryFields() {
    List<String> messages = new ArrayList<>();
    messages.add(WalkingDifficultyPage.VALIDATION_MESSAGE_FOR_NO_OPTION);
    commonSteps.iVerifyMultipleValidationMessages(messages);
  }


  @And("I complete what makes walking difficult page for \"(HELP|PLAN|PAIN|DANGEROUS|NONE)\"$")
  public void enterValidValuesAndContinue(String difficulty) {

    if ("HELP".equals(difficulty)) {
      commonPage.selectRadioButton(WhatMakesWalkingDifficultiesPage.WALKING_DIFFICULTY_LIST);
    } else {
      commonPage.selectRadioButton(WhatMakesWalkingDifficultiesPage.WALKING_DIFFICULTY_LIST + "." + difficulty);
    }

    commonSteps.iClickOnContinueButton();
  }


}
