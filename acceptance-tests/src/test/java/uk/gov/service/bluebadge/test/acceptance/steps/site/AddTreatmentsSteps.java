package uk.gov.service.bluebadge.test.acceptance.steps.site;

import com.google.common.collect.Lists;
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
    messages.add(AddTreatmentsPage.VALIDATION_MESSAGE_FOR_NULL_WHEN_TYPE);
    commonSteps.iVerifyMultipleValidationMessagesInChildPages(
        messages, AddTreatmentsPage.TREATMENT_ADD_BUTTON);
  }

  private void validateInvalidEntries() {

    //To validate am invalid Description
    commonPage.findPageElementById(AddTreatmentsPage.TREATMENT_DESCRIPTION).click();
    commonPage
        .findPageElementById(AddTreatmentsPage.TREATMENT_DESCRIPTION)
        .sendKeys(AddTreatmentsPage.INVALID_TEXT);
    commonPage.selectRadioButton(AddTreatmentsPage.TREATMENT_PAST_OPTION);
    commonPage.findPageElementById(AddTreatmentsPage.TREATMENT_PAST_WHEN).click();
    commonPage
        .findPageElementById(AddTreatmentsPage.TREATMENT_PAST_WHEN)
        .sendKeys(AddTreatmentsPage.VALID_WHEN);
    commonSteps.iVerifyValidationMessage(
        AddTreatmentsPage.VALIDATION_MESSAGE_FOR_INVALID_DESCRIPTION);

    //To validate null 'Past When'
    commonPage.findPageElementById(AddTreatmentsPage.TREATMENT_DESCRIPTION).click();
    commonPage.findPageElementById(AddTreatmentsPage.TREATMENT_DESCRIPTION).clear();
    commonPage
        .findPageElementById(AddTreatmentsPage.TREATMENT_DESCRIPTION)
        .sendKeys(AddTreatmentsPage.VALID_DESCRIPTION);
    // (null)
    commonPage.selectRadioButton(AddTreatmentsPage.TREATMENT_PAST_OPTION);
    commonPage.findPageElementById(AddTreatmentsPage.TREATMENT_PAST_WHEN).click();
    commonPage.findPageElementById(AddTreatmentsPage.TREATMENT_PAST_WHEN).clear();
    commonSteps.iVerifyValidationMessage(AddTreatmentsPage.VALIDATION_MESSAGE_FOR_NULL_PAST_WHEN);
    // (invalid)
    commonPage.findPageElementById(AddTreatmentsPage.TREATMENT_PAST_WHEN).click();
    commonPage
        .findPageElementById(AddTreatmentsPage.TREATMENT_PAST_WHEN)
        .sendKeys(AddTreatmentsPage.INVALID_TEXT);
    commonSteps.iVerifyValidationMessage(
        AddTreatmentsPage.VALIDATION_MESSAGE_FOR_INVALID_PAST_WHEN);

    //To validate null and invalid 'Ongoing Frequency'
    commonPage.findPageElementById(AddTreatmentsPage.TREATMENT_DESCRIPTION).click();
    commonPage.findPageElementById(AddTreatmentsPage.TREATMENT_DESCRIPTION).clear();
    commonPage
        .findPageElementById(AddTreatmentsPage.TREATMENT_DESCRIPTION)
        .sendKeys(AddTreatmentsPage.VALID_DESCRIPTION);
    // (null)
    commonPage.selectRadioButton(AddTreatmentsPage.TREATMENT_ONGOING_OPTION);
    commonPage.findPageElementById(AddTreatmentsPage.TREATMENT_ONGOING_FREQUENCY).click();
    commonPage.findPageElementById(AddTreatmentsPage.TREATMENT_ONGOING_FREQUENCY).clear();
    commonSteps.iVerifyValidationMessage(
        AddTreatmentsPage.VALIDATION_MESSAGE_FOR_NULL_ONGOING_FREQUENCY);
    // (invalid)
    commonPage.findPageElementById(AddTreatmentsPage.TREATMENT_ONGOING_FREQUENCY).click();
    commonPage
        .findPageElementById(AddTreatmentsPage.TREATMENT_ONGOING_FREQUENCY)
        .sendKeys(AddTreatmentsPage.INVALID_TEXT);
    commonSteps.iVerifyValidationMessage(
        AddTreatmentsPage.VALIDATION_MESSAGE_FOR_INVALID_ONGOING_FREQUENCY);

    //To validate null and invalid 'Future When and Improvement'
    commonPage.findPageElementById(AddTreatmentsPage.TREATMENT_DESCRIPTION).click();
    commonPage.findPageElementById(AddTreatmentsPage.TREATMENT_DESCRIPTION).clear();
    commonPage
        .findPageElementById(AddTreatmentsPage.TREATMENT_DESCRIPTION)
        .sendKeys(AddTreatmentsPage.VALID_DESCRIPTION);
    // (null When)
    commonPage.selectRadioButton(AddTreatmentsPage.TREATMENT_FUTURE_OPTION);
    commonPage.findPageElementById(AddTreatmentsPage.TREATMENT_FUTURE_WHEN).click();
    commonPage.findPageElementById(AddTreatmentsPage.TREATMENT_FUTURE_WHEN).clear();
    commonSteps.iVerifyMultipleValidationMessages(
        Lists.newArrayList(
            AddTreatmentsPage.VALIDATION_MESSAGE_FOR_NULL_FUTURE_WHEN,
            AddTreatmentsPage.VALIDATION_MESSAGE_FOR_NULL_FUTURE_IMPROVE));
    // (invalid When)
    commonPage.findPageElementById(AddTreatmentsPage.TREATMENT_FUTURE_WHEN).click();
    commonPage
        .findPageElementById(AddTreatmentsPage.TREATMENT_FUTURE_WHEN)
        .sendKeys(AddTreatmentsPage.INVALID_TEXT);
    commonSteps.iVerifyMultipleValidationMessages(
        Lists.newArrayList(
            AddTreatmentsPage.VALIDATION_MESSAGE_FOR_INVALID_FUTURE_WHEN,
            AddTreatmentsPage.VALIDATION_MESSAGE_FOR_NULL_FUTURE_IMPROVE));
    // (null Improvement)
    commonPage.findPageElementById(AddTreatmentsPage.TREATMENT_FUTURE_WHEN).click();
    commonPage
        .findPageElementById(AddTreatmentsPage.TREATMENT_FUTURE_WHEN)
        .sendKeys(AddTreatmentsPage.VALID_WHEN);
    commonPage.findPageElementById(AddTreatmentsPage.TREATMENT_FUTURE_IMPROVE).click();
    commonPage.findPageElementById(AddTreatmentsPage.TREATMENT_FUTURE_IMPROVE).clear();
    commonSteps.iVerifyMultipleValidationMessages(
        Lists.newArrayList(
            AddTreatmentsPage.VALIDATION_MESSAGE_FOR_INVALID_FUTURE_WHEN,
            AddTreatmentsPage.VALIDATION_MESSAGE_FOR_NULL_FUTURE_IMPROVE));
    // (invalid Improvement)
    commonPage.findPageElementById(AddTreatmentsPage.TREATMENT_FUTURE_IMPROVE).click();
    commonPage
        .findPageElementById(AddTreatmentsPage.TREATMENT_FUTURE_IMPROVE)
        .sendKeys(AddTreatmentsPage.INVALID_TEXT);
    commonSteps.iVerifyMultipleValidationMessages(
        Lists.newArrayList(
            AddTreatmentsPage.VALIDATION_MESSAGE_FOR_INVALID_FUTURE_WHEN,
            AddTreatmentsPage.VALIDATION_MESSAGE_FOR_INVALID_FUTURE_IMPROVE));
  }

  @And("^I complete add treatments page \"$")
  public void iCompleteAddTreatmentsPage() {
    commonPage.clearAndSendKeys(
        AddTreatmentsPage.TREATMENT_DESCRIPTION, AddTreatmentsPage.VALID_DESCRIPTION);
    commonPage.selectRadioButton(AddTreatmentsPage.TREATMENT_PAST_OPTION);
    commonPage.clearAndSendKeys(
        AddTreatmentsPage.TREATMENT_PAST_WHEN, AddTreatmentsPage.VALID_WHEN);
    commonSteps.iClickOnAddButtonOnChildPage(AddTreatmentsPage.TREATMENT_ADD_BUTTON);
  }
}
