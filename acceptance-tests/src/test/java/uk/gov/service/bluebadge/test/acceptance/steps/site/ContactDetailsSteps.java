package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.ContactDetailsPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.EnterAddressPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

import java.util.ArrayList;
import java.util.List;

public class ContactDetailsSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;

  @Autowired
  public ContactDetailsSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }

  @And("^I validate contact details page for a \"(yourself|someone else)\" application$")
  public void iValidateContactDetailsPageForAApplication(String applicant) {
    verifyPageContent(applicant);
    validateMandatoryFields(applicant);
    validateInvalidValues(applicant);
    enterValidValuesAndContinue(applicant);

  }

  public void verifyPageContent(String applicant) {

    commonSteps.iShouldSeeTheCorrectURL(ContactDetailsPage.PAGE_URL);

    if ("yourself".equals(applicant.toLowerCase())) {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(ContactDetailsPage.PAGE_TITLE);
      commonSteps.iShouldSeeTheHeading(ContactDetailsPage.PAGE_TITLE);
    } else {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(ContactDetailsPage.PAGE_TITLE);
      commonSteps.iShouldSeeTheHeading(ContactDetailsPage.PAGE_TITLE);
    }
  }

  private void validateMandatoryFields(String applicant) {
    List<String> messages = new ArrayList<>();
    messages.add(ContactDetailsPage.VALIDATION_MESSAGE_FOR_EMPTY_PHONE_NUMBER);
    if("someone else".equals(applicant)) {
      messages.add(ContactDetailsPage.VALIDATION_MESSAGE_FOR_CONTACT_FULL_NAME);
    }

    commonSteps.iVerifyMultipleValidationMessages(messages);
  }

  private void validateInvalidValues(String applicant) {
    List<String> messages = new ArrayList<>();
    commonPage.clearAndSendKeys(ContactDetailsPage.PRIMARY_CONTACT_NUMBER,"020 12212123 000");
    commonPage.clearAndSendKeys(ContactDetailsPage.SECONDARY_CONTACT_NUMBER,"078 13345456 000");
    commonPage.clearAndSendKeys(ContactDetailsPage.EMAIL_ADDRESS,"test@testmail.com");

    messages.add(ContactDetailsPage.VALIDATION_MESSAGE_FOR_INVALID_MAIN_PHONE_NUMBER);
    messages.add(ContactDetailsPage.VALIDATION_MESSAGE_FOR_INVALID_ALTERNATIVE_PHONE_NUMBER);
    messages.add(ContactDetailsPage.VALIDATION_MESSAGE_FOR_INVALID_ALTERNATIVE_PHONE_NUMBER);

    if ("someone else".equals(applicant.toLowerCase())) {
      commonPage.clearAndSendKeys(ContactDetailsPage.FULL_NAME,"£$%^&* Invalid Name");
      messages.add(ContactDetailsPage.VALIDATION_MESSAGE_FOR_INVALID_FULLNAME);
    }

    commonSteps.iVerifyMultipleValidationMessages(messages);
    commonSteps.iClickOnContinueButton();
  }

  public void enterValidValuesAndContinue(String applicant) {
    if ("someone else".equals(applicant.toLowerCase())) {
      commonPage.clearAndSendKeys(ContactDetailsPage.FULL_NAME,"Test Contact");
    }

    commonPage.clearAndSendKeys(ContactDetailsPage.PRIMARY_CONTACT_NUMBER,"020 12212123");
    commonPage.clearAndSendKeys(ContactDetailsPage.SECONDARY_CONTACT_NUMBER,"078 13345456");
    commonPage.clearAndSendKeys(ContactDetailsPage.EMAIL_ADDRESS,"test@testmail.com");

    commonSteps.iClickOnContinueButton();
  }
}
