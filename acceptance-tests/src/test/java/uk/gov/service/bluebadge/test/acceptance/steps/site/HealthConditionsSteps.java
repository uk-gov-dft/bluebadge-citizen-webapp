package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.And;
import gherkin.lexer.He;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.ContactDetailsPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.HealthConditionsPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

import java.util.ArrayList;
import java.util.List;

public class HealthConditionsSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;

  @Autowired
  public HealthConditionsSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }

  @And("^I validate health conditions page for a \"(yourself|someone else)\" application$")
  public void iValidateHealthConditionsPageForAApplication(String applicant) {
    verifyPageContent(applicant);
    validateMandatoryFields();

    enterValidValuesAndContinue(applicant);

  }

  public void verifyPageContent(String applicant) {

    commonSteps.iShouldSeeTheCorrectURL(HealthConditionsPage.PAGE_URL);

    if ("yourself".equals(applicant.toLowerCase())) {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(HealthConditionsPage.PAGE_TITLE);
      commonSteps.iShouldSeeTheHeading(HealthConditionsPage.PAGE_HEADER);
    } else {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(HealthConditionsPage.PAGE_TITLE);
      commonSteps.iShouldSeeTheHeading(HealthConditionsPage.PAGE_HEADER_SOMEONE_ELSE);
    }
  }

  private void validateMandatoryFields() {
    List<String> messages = new ArrayList<>();
    messages.add(HealthConditionsPage.VALIDATION_MESSAGE_FOR_EMPTY_DESCRIPTION);
    commonSteps.iVerifyMultipleValidationMessages(messages);
  }


  @And("I complete health conditions page for \"(yourself|someone else)\"")
  public void enterValidValuesAndContinue(String applicant) {
    commonPage.clearAndSendKeys(HealthConditionsPage.DESCRIPTION_ID,HealthConditionsPage.VALID_DESCRIPTION);
    commonSteps.iClickOnContinueButton();
  }
}
