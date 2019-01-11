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
  List<String> messages = new ArrayList<>();

  @Autowired
  public EnterAddressSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }

  @And("^I validate enter address page for a \"(yourself|someone else)\" application$")
  public void iValidateEnterAddressPageForAApplication(String applicant) {
    verifyPageContent(applicant);
    validateMandotaryFields(applicant);
    validateLengthLimitBuildingAndStreet(applicant);
    validateLengthLimitTownAndCity(applicant);
    validateInvalidPostcode(applicant);
    enterValidValuesAndContinue(applicant);
  }

  public void verifyPageContent(String applicant) {

    commonSteps.iShouldSeeTheCorrectURL(EnterAddressPage.PAGE_URL);

    if ("yourself".equals(applicant.toLowerCase())) {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(EnterAddressPage.PAGE_TITLE_YOURSELF);
      commonSteps.iShouldSeeTheHeading(EnterAddressPage.PAGE_TITLE_YOURSELF);
    } else {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(EnterAddressPage.PAGE_TITLE_SOMEONE_ELSE);
      commonSteps.iShouldSeeTheHeading(EnterAddressPage.PAGE_TITLE_SOMEONE_ELSE);
    }
  }

  private void validateMandotaryFields(String applicant) {

    messages.add(EnterAddressPage.VALIDATION_MESSAGE_FOR_EMPTY_BUILDING_AND_STREET);
    messages.add(EnterAddressPage.VALIDATION_MESSAGE_FOR_EMPTY_TOWN_CITY);
    messages.add(EnterAddressPage.VALIDATION_MESSAGE_FOR_EMPTY_POSTCODE);
    commonSteps.iVerifyMultipleValidationMessages(messages);
  }

  private void validateLengthLimitBuildingAndStreet(String applicant) {

    commonPage
        .findPageElementById("buildingAndStreet")
        .sendKeys(EnterAddressPage.GREATER_THAN_100_CHARACTERS);
    commonPage.findPageElementById("townOrCity").sendKeys(EnterAddressPage.VALID_TOWN);
    commonPage.findPageElementById("postcode").sendKeys(EnterAddressPage.VALID_POSTCODE);
    commonSteps.iVerifyValidationMessage(
        EnterAddressPage.VALIDATION_MESSAGE_FOR_GT100_BUILDING_AND_STREET);
  }

  private void validateLengthLimitTownAndCity(String applicant) {
    commonPage.clearAndSendKeys("buildingAndStreet", EnterAddressPage.VALID_BUILDING_STREET);
    commonPage.clearAndSendKeys("townOrCity", EnterAddressPage.GREATER_THAN_100_CHARACTERS);
    commonPage.clearAndSendKeys("postcode", EnterAddressPage.VALID_POSTCODE);

    commonSteps.iVerifyValidationMessage(EnterAddressPage.VALIDATION_MESSAGE_FOR_GT100_TOWN_CITY);
  }

  private void validateInvalidPostcode(String applicant) {
    commonPage.clearAndSendKeys("buildingAndStreet", EnterAddressPage.VALID_BUILDING_STREET);
    commonPage.clearAndSendKeys("townOrCity", EnterAddressPage.VALID_TOWN);
    commonPage.clearAndSendKeys("postcode", EnterAddressPage.INVALID_POSTCODE);

    commonSteps.iVerifyValidationMessage(EnterAddressPage.VALIDATION_MESSAGE_FOR_INVALID_POSTCODE);
  }

  private void enterValidValuesAndContinue(String applicant) {
    commonPage.clearAndSendKeys("buildingAndStreet", EnterAddressPage.VALID_BUILDING_STREET);
    commonPage.clearAndSendKeys("townOrCity", EnterAddressPage.VALID_TOWN);
    commonPage.clearAndSendKeys("postcode", EnterAddressPage.VALID_POSTCODE);

    commonSteps.iClickOnContinueButton();
  }
}
