package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.And;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.HealthConditionsPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class HealthConditionsSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;

  @Autowired
  public HealthConditionsSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }

  @And(
      "^I validate health conditions page for a \"(yourself|someone else)\" application and eligibility \"(PIP|DLA|AFRFCS|WPMS|BLIND|WALKD|ARMS|CHILDBULK|CHILDVEHIC|TERMILL|NONE)\"$")
  public void iValidateHealthConditionsPageForAApplication(String applicant, String eligibility) {
    verifyPageContent(applicant, eligibility);
    validateMandatoryFields();

    enterValidValuesAndContinue(applicant);
  }

  public void verifyPageContent(String applicant, String eligibility) {

    commonSteps.iShouldSeeTheCorrectURL(HealthConditionsPage.PAGE_URL);
    commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(HealthConditionsPage.PAGE_TITLE);

    if ("yourself".equals(applicant.toLowerCase())) {
      if ("WALKD".equals(eligibility)) {
        commonSteps.iShouldSeeTheHeading(HealthConditionsPage.PAGE_HEADER_WALKING);
      } else {
        commonSteps.iShouldSeeTheHeading(HealthConditionsPage.PAGE_HEADER);
      }
    } else {
      if ("WALKD".equals(eligibility)) {
        commonSteps.iShouldSeeTheHeading(HealthConditionsPage.PAGE_HEADER_SOMEONE_ELSE_WALKING);
      } else {
        commonSteps.iShouldSeeTheHeading(HealthConditionsPage.PAGE_HEADER_SOMEONE_ELSE);
      }
    }
  }

  private void validateMandatoryFields() {
    List<String> messages = new ArrayList<>();
    messages.add(HealthConditionsPage.VALIDATION_MESSAGE_FOR_EMPTY_DESCRIPTION);
    commonSteps.iVerifyMultipleValidationMessages(messages);
  }

  @And("I complete health conditions page for \"(yourself|someone else)\"")
  public void enterValidValuesAndContinue(String applicant) {
    commonPage.clearAndSendKeys(
        HealthConditionsPage.DESCRIPTION_ID, HealthConditionsPage.VALID_DESCRIPTION);
    commonSteps.iClickOnContinueButton();
  }
}
