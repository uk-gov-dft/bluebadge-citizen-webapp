package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.And;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.WhatMakesWalkingDifficultiesPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class WhatMakesWalkingDifficultSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;

  @Autowired
  public WhatMakesWalkingDifficultSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }

  @And(
      "^I validate what makes walking difficult page for a \"(yourself|someone else)\" application for \"(PAIN|BREATH|BALANCE|LONGTIME|DANGER|STRUGGLE|SOMELSE)\"$")
  public void iValidateWhatMakesWalkingDifficultPageForAApplication(
      String applicant, String difficulty) {

    verifyPageContent(applicant);
    validateMandatoryFields();

    iCompleteTheWhatMakesWalkingDifficultPageFor(difficulty);
  }

  public void verifyPageContent(String applicant) {

    commonSteps.iShouldSeeTheCorrectURL(WhatMakesWalkingDifficultiesPage.PAGE_URL);

    if ("yourself".equals(applicant.toLowerCase())) {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(
          WhatMakesWalkingDifficultiesPage.PAGE_TITLE);
      commonSteps.iShouldSeeTheHeading(WhatMakesWalkingDifficultiesPage.PAGE_TITLE);
    } else {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(
          WhatMakesWalkingDifficultiesPage.PAGE_TITLE_SOMEONE_ELSE);
      commonSteps.iShouldSeeTheHeading(WhatMakesWalkingDifficultiesPage.PAGE_TITLE_SOMEONE_ELSE);
    }
  }

  private void validateMandatoryFields() {
    List<String> messages = new ArrayList<>();
    messages.add(WhatMakesWalkingDifficultiesPage.VALIDATION_MESSAGE_FOR_NO_OPTION);
    commonSteps.iVerifyMultipleValidationMessages(messages);
  }

  @And("^I complete the what makes walking difficult page$")
  public void iCompleteTheWhatMakesWalkingDifficultPage() {
    commonPage.selectRadioButton(WhatMakesWalkingDifficultiesPage.WHAT_WALKING_DIFFICULTY_LIST);
    commonSteps.iClickOnContinueButton();
  }

  @And(
      "^I complete the what makes walking difficult page for \"(PAIN|BREATH|BALANCE|LONGTIME|DANGER|STRUGGLE|SOMELSE)\"$")
  public void iCompleteTheWhatMakesWalkingDifficultPageFor(String difficulty) {
    if ("PAIN".equals(difficulty)) {
      commonPage.selectRadioButton(WhatMakesWalkingDifficultiesPage.WHAT_WALKING_DIFFICULTY_LIST);
    } else {
      commonPage.selectRadioButton(
          WhatMakesWalkingDifficultiesPage.WHAT_WALKING_DIFFICULTY_LIST + difficulty);
    }
    commonSteps.iClickOnContinueButton();
  }
}
