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
  List<String> messages = new ArrayList<>();

  @Autowired
  public ContactDetailsSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }


  @And("^I validate contact details page for a \"(yourself|someone else)\" application$")
  public void iValidateContactDetailsPageForAApplication(String applicant) {
    verifyPageContent(applicant);
    validateMandatoryFields(applicant);





    //To validate length limit <=100 characters for Building and Street Field
    commonPage
            .findPageElementById("buildingAndStreet")
            .sendKeys(EnterAddressPage.GREATER_THAN_100_CHARACTERS);
    commonPage.findPageElementById("townOrCity").sendKeys(EnterAddressPage.VALID_TOWN);
    commonPage.findPageElementById("postcode").sendKeys(EnterAddressPage.VALID_POSTCODE);
    commonSteps.iVerifyValidationMessage(
            EnterAddressPage.VALIDATION_MESSAGE_FOR_GT100_BUILDING_AND_STREET);

    //To validate length limit <=100 characters for Town/City
    commonPage.findPageElementById("buildingAndStreet").clear();
    commonPage.findPageElementById("townOrCity").clear();
    commonPage.findPageElementById("postcode").clear();
    commonPage
            .findPageElementById("buildingAndStreet")
            .sendKeys(EnterAddressPage.VALID_BUILDING_STREET);
    commonPage
            .findPageElementById("townOrCity")
            .sendKeys(EnterAddressPage.GREATER_THAN_100_CHARACTERS);
    commonPage.findPageElementById("postcode").sendKeys(EnterAddressPage.VALID_POSTCODE);
    commonSteps.iVerifyValidationMessage(EnterAddressPage.VALIDATION_MESSAGE_FOR_GT100_TOWN_CITY);

    //To validate Invalid  post code field
    commonPage.findPageElementById("buildingAndStreet").clear();
    commonPage.findPageElementById("townOrCity").clear();
    commonPage.findPageElementById("postcode").clear();
    commonPage
            .findPageElementById("buildingAndStreet")
            .sendKeys(EnterAddressPage.VALID_BUILDING_STREET);
    commonPage.findPageElementById("townOrCity").sendKeys(EnterAddressPage.VALID_TOWN);
    commonPage.findPageElementById("postcode").sendKeys(EnterAddressPage.INVALID_POSTCODE);
    commonSteps.iVerifyValidationMessage(EnterAddressPage.VALIDATION_MESSAGE_FOR_INVALID_POSTCODE);

    //Enter valid values and Continue
    commonPage.findPageElementById("buildingAndStreet").clear();
    commonPage.findPageElementById("townOrCity").clear();
    commonPage.findPageElementById("postcode").clear();
    commonPage
            .findPageElementById("buildingAndStreet")
            .sendKeys(EnterAddressPage.VALID_BUILDING_STREET);
    commonPage.findPageElementById("townOrCity").sendKeys(EnterAddressPage.VALID_TOWN);
    commonPage.findPageElementById("postcode").sendKeys(EnterAddressPage.VALID_POSTCODE);

    commonSteps.iClickOnContinueButton();
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
    messages.add(ContactDetailsPage.VALIDATION_MESSAGE_FOR_EMPTY_PHONE_NUMBER);
    if("someone else".equals(applicant)) {
      messages.add(ContactDetailsPage.VALIDATION_MESSAGE_FOR_CONTACT_FULL_NAME);
    }

    commonSteps.iVerifyMultipleValidationMessages(messages);
  }
}
