package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.And;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.AddMedicationPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class AddMedicationSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;

  @Autowired
  public AddMedicationSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }

  @And("^I validate the add medication page \"$")
  public void iValidateAddMedicationPage() {
    verifyPageContent();
    validateMandatoryFields();
    iCompleteAddMedicationPage();
  }

  public void verifyPageContent() {

    commonSteps.iShouldSeeTheCorrectURL(AddMedicationPage.PAGE_URL);
    commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(AddMedicationPage.PAGE_TITLE);
  }

  private void validateMandatoryFields() {
    List<String> messages = new ArrayList<>();
    messages.add(AddMedicationPage.VALIDATION_MESSAGE_FOR_EMPTY_DOSAGE);
    messages.add(AddMedicationPage.VALIDATION_MESSAGE_FOR_EMPTY_FREQUENCY);
    messages.add(AddMedicationPage.VALIDATION_MESSAGE_FOR_EMPTY_NAME);
    messages.add(AddMedicationPage.VALIDATION_MESSAGE_FOR_EMPTY_PRESCRIBED);
    commonSteps.iVerifyMultipleValidationMessagesInChildPages(
        messages, AddMedicationPage.MEDICATION_ADD_BUTTON);
  }

  @And("^I complete add medication page \"$")
  public void iCompleteAddMedicationPage() {
    commonPage.clearAndSendKeys(AddMedicationPage.NAME, AddMedicationPage.VALID_NAME);
    commonPage.clearAndSendKeys(AddMedicationPage.DOSAGE, AddMedicationPage.VALID_DOSAGE);
    commonPage.clearAndSendKeys(AddMedicationPage.FREQUENCY, AddMedicationPage.VALID_FREQUENCY);
    commonPage.findPageElementById(AddMedicationPage.PRESCRIBED).click();
    commonSteps.iClickOnAddButtonOnChildPage(AddMedicationPage.MEDICATION_ADD_BUTTON);
  }
}
