package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.And;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.EnterAddressPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class EnterAddressSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;

  @Autowired
  public EnterAddressSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }

  @And("^I validate enter address page for a \"([^\"]*)\" application$")
  public void iValidateEnterAddressPageForAApplication(String applicant) {
    verifyPageContent(applicant);

    //To validate Continue without entering data in the address fields
    List<String> messages = new ArrayList<>();
    messages.add(EnterAddressPage.VALIDATION_MESSAGE_FOR_EMPTY_BUILDING_AND_STREET);
    messages.add(EnterAddressPage.VALIDATION_MESSAGE_FOR_EMPTY_TOWN_CITY);
    messages.add(EnterAddressPage.VALIDATION_MESSAGE_FOR_EMPTY_POSTCODE);
    commonSteps.iVerifyMultipleValidationMessages(messages);

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

    commonSteps.iShouldSeeTheCorrectURL(EnterAddressPage.PAGE_URL);

    if ("you".equals(applicant.toLowerCase()) | "self".equals(applicant.toLowerCase())) {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(EnterAddressPage.PAGE_TITLE_YOURSELF);
      commonSteps.iShouldSeeTheHeading(EnterAddressPage.PAGE_TITLE_YOURSELF);
    } else {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(EnterAddressPage.PAGE_TITLE_SOMEONE_ELSE);
      commonSteps.iShouldSeeTheHeading(EnterAddressPage.PAGE_TITLE_SOMEONE_ELSE);
    }
  }
}
