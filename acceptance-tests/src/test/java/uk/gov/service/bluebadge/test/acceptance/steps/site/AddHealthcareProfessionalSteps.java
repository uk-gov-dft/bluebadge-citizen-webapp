package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.And;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.AddHealthcareProfessionalPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class AddHealthcareProfessionalSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;

  @Autowired
  public AddHealthcareProfessionalSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }

  @And("^I validate the add healthcare professional page \"$")
  public void iValidateAddHealthcareProfessionalPage() {
    verifyPageContent();
    validateMandatoryFields();
    validateInvalidEntries();
    iCompleteAddHealthcareProfessionalPage();
  }

  public void verifyPageContent() {

    commonSteps.iShouldSeeTheCorrectURL(AddHealthcareProfessionalPage.PAGE_URL);
    commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(AddHealthcareProfessionalPage.PAGE_TITLE);
  }

  private void validateMandatoryFields() {
    List<String> messages = new ArrayList<>();
    messages.add(AddHealthcareProfessionalPage.VALIDATION_MESSAGE_FOR_EMPTY_NAME);
    messages.add(AddHealthcareProfessionalPage.VALIDATION_MESSAGE_FOR_EMPTY_LOCATION);
    commonSteps.iVerifyMultipleValidationMessagesInChildPages(
        messages, AddHealthcareProfessionalPage.HC_PROFESSIONAL_ADD_BUTTON);
  }

  private void validateInvalidEntries() {

    //To validate am invalid Name
    commonPage.findPageElementById(AddHealthcareProfessionalPage.NAME).click();
    commonPage
        .findPageElementById(AddHealthcareProfessionalPage.NAME)
        .sendKeys(AddHealthcareProfessionalPage.INVALID_TEXT);
    commonPage.findPageElementById(AddHealthcareProfessionalPage.LOCATION).click();
    commonPage
        .findPageElementById(AddHealthcareProfessionalPage.LOCATION)
        .sendKeys(AddHealthcareProfessionalPage.VALID_LOCATION);
    commonSteps.iVerifyValidationMessage(
        AddHealthcareProfessionalPage.VALIDATION_MESSAGE_FOR_INVALID_NAME);

    //To validate am invalid location
    commonPage.findPageElementById(AddHealthcareProfessionalPage.NAME).click();
    commonPage.findPageElementById(AddHealthcareProfessionalPage.NAME).clear();
    commonPage
        .findPageElementById(AddHealthcareProfessionalPage.NAME)
        .sendKeys(AddHealthcareProfessionalPage.VALID_NAME);
    commonPage.findPageElementById(AddHealthcareProfessionalPage.LOCATION).click();
    commonPage
        .findPageElementById(AddHealthcareProfessionalPage.LOCATION)
        .sendKeys(AddHealthcareProfessionalPage.INVALID_TEXT);
    commonSteps.iVerifyValidationMessage(
        AddHealthcareProfessionalPage.VALIDATION_MESSAGE_FOR_INVALID_LOCATION);
  }

  @And("^I complete add healthcare professional page \"$")
  public void iCompleteAddHealthcareProfessionalPage() {
    commonPage.clearAndSendKeys(
        AddHealthcareProfessionalPage.NAME, AddHealthcareProfessionalPage.VALID_NAME);
    commonPage.clearAndSendKeys(
        AddHealthcareProfessionalPage.LOCATION, AddHealthcareProfessionalPage.VALID_LOCATION);
    commonSteps.iClickOnAddButtonOnChildPage(
        AddHealthcareProfessionalPage.HC_PROFESSIONAL_ADD_BUTTON);
  }
}
