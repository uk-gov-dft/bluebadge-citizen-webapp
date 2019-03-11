package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.And;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.BreathlessnessPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class BreathlessnessSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;

  @Autowired
  public BreathlessnessSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }

  @And(
      "^I validate breathlessness page for a \"(yourself|someone else)\" application for \"(UPHILL|OWNPACE|KEEPUP|DRESSED|OTHER)\"$")
  public void iValidateBreathlessnessPageForAApplication(String applicant, String difficulty) {

    verifyPageContent(applicant);
    validateMandatoryFields();

    iCompleteTheWhenDoYouGetBreathlessPageFor(difficulty);
  }

  public void verifyPageContent(String applicant) {

    commonSteps.iShouldSeeTheCorrectURL(BreathlessnessPage.PAGE_URL);

    if ("yourself".equals(applicant.toLowerCase())) {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(BreathlessnessPage.PAGE_TITLE);
      commonSteps.iShouldSeeTheHeading(BreathlessnessPage.PAGE_TITLE);
    } else {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(
          BreathlessnessPage.PAGE_TITLE_SOMEONE_ELSE);
      commonSteps.iShouldSeeTheHeading(BreathlessnessPage.PAGE_TITLE_SOMEONE_ELSE);
    }
  }

  private void validateMandatoryFields() {
    List<String> messages = new ArrayList<>();
    messages.add(BreathlessnessPage.VALIDATION_MESSAGE_FOR_NO_OPTION);
    commonSteps.iVerifyMultipleValidationMessages(messages);
  }

  @And("^I complete the breathlessness page for \"(UPHILL|OWNPACE|KEEPUP|DRESSED|OTHER)\"$")
  public void iCompleteTheWhenDoYouGetBreathlessPageFor(String difficulty) {
    if ("UPHILL".equals(difficulty)) {
      commonPage.selectRadioButton(BreathlessnessPage.BREATHLESSNESS_TYPES);
    } else {
      commonPage.selectRadioButton(BreathlessnessPage.BREATHLESSNESS_TYPES + difficulty);
    }

    if (!"OTHER".equals(difficulty)) {
      commonSteps.iClickOnContinueButton();
    }
  }
}
