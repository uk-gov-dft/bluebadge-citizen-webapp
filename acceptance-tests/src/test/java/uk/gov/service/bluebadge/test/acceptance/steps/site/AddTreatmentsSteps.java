package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.And;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.AddTreatmentsPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class AddTreatmentsSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;

  @Autowired
  public AddTreatmentsSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }

  @And("^I validate the add treatments page \"$")
  public void iValidateAddTreatmentsPage() {
    verifyPageContent();
    validateMandatoryFields();
    validateInvalidEntries();
    iCompleteAddTreatmentsPage();
  }

  public void verifyPageContent() {

    commonSteps.iShouldSeeTheCorrectURL(AddTreatmentsPage.PAGE_URL);
    commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(AddTreatmentsPage.PAGE_TITLE);
  }

  private void validateMandatoryFields() {
    List<String> messages = new ArrayList<>();
    messages.add(AddTreatmentsPage.VALIDATION_MESSAGE_FOR_EMPTY_TREATMENT_DESCRIPTION);
    messages.add(AddTreatmentsPage.VALIDATION_MESSAGE_FOR_EMPTY_TREATMENT_WHEN);
    commonSteps.iVerifyMultipleValidationMessagesInChildPages(
        messages, AddTreatmentsPage.TREATMENT_ADD_BUTTON);
  }

  private void validateInvalidEntries() {

    //To validate am invalid Description
    commonPage.findPageElementById(AddTreatmentsPage.TREATMENT_DESCRIPTION).click();
    commonPage
        .findPageElementById(AddTreatmentsPage.TREATMENT_DESCRIPTION)
        .sendKeys(AddTreatmentsPage.INVALID_TEXT);
    commonPage.findPageElementById(AddTreatmentsPage.TREATMENT_WHEN).click();
    commonPage
        .findPageElementById(AddTreatmentsPage.TREATMENT_WHEN)
        .sendKeys(AddTreatmentsPage.VALID_WHEN);
    commonSteps.iVerifyValidationMessage(
        AddTreatmentsPage.VALIDATION_MESSAGE_FOR_INVALID_DESCRIPTION);

    //To validate am invalid When
    commonPage.findPageElementById(AddTreatmentsPage.TREATMENT_DESCRIPTION).click();
    commonPage.findPageElementById(AddTreatmentsPage.TREATMENT_DESCRIPTION).clear();
    commonPage
        .findPageElementById(AddTreatmentsPage.TREATMENT_DESCRIPTION)
        .sendKeys(AddTreatmentsPage.VALID_DESCRIPTION);
    commonPage.findPageElementById(AddTreatmentsPage.TREATMENT_WHEN).click();
    commonPage
        .findPageElementById(AddTreatmentsPage.TREATMENT_WHEN)
        .sendKeys(AddTreatmentsPage.INVALID_TEXT);
    commonSteps.iVerifyValidationMessage(AddTreatmentsPage.VALIDATION_MESSAGE_FOR_INVALID_WHEN);
  }

  @And("^I complete add treatments page \"$")
  public void iCompleteAddTreatmentsPage() {
    commonPage.clearAndSendKeys(
        AddTreatmentsPage.TREATMENT_DESCRIPTION, AddTreatmentsPage.VALID_DESCRIPTION);
    commonPage.clearAndSendKeys(AddTreatmentsPage.TREATMENT_WHEN, AddTreatmentsPage.VALID_WHEN);
    commonSteps.iClickOnAddButtonOnChildPage(AddTreatmentsPage.TREATMENT_ADD_BUTTON);
  }
}
