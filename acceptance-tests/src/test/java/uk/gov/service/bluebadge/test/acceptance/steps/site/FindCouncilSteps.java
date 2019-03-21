package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.FindCouncilPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class FindCouncilSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;
  private String journeyOption;

  @Autowired
  public FindCouncilSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }

  @And(
      "^I complete find council page for \"(yourself|someone else)\" and select a postcode in \"(england|wales|scotland)\"")
  public void iCompleteFindCouncilPageForAndSelectACouncil(String applicant, String country) {
    String postcode = findPostcode(country, false);
    verifyPageContent(applicant);
    commonPage.findElementWithUiPath(FindCouncilPage.POSTCODE_INPUT).clear();
    commonPage.findElementWithUiPath(FindCouncilPage.POSTCODE_INPUT).sendKeys(postcode);
    commonSteps.iClickOnContinueButton();
  }

  @And(
      "^I complete find council page for \"(yourself|someone else)\" and la using third party and select a postcode in \"(england|wales|scotland)\"")
  public void iCompleteFindCouncilPageForUsingThirdPartyAndSelectACouncil(
      String applicant, String country) {
    String postcode = findPostcode(country, true);
    verifyPageContent(applicant);
    commonPage.findElementWithUiPath(FindCouncilPage.POSTCODE_INPUT).clear();
    commonPage.findElementWithUiPath(FindCouncilPage.POSTCODE_INPUT).sendKeys(postcode);
    commonSteps.iClickOnContinueButton();
  }

  @And(
      "^I validate find council page for \"(yourself|someone else)\" and select a postcode in \"(england|wales|scotland)\"")
  public void iValidateFindCouncilPageForAndSelectACouncil(String applicant, String country) {
    String postcode = findPostcode(country, false);
    verifyPageContent(applicant);
    commonSteps.iVerifyValidationMessage(FindCouncilPage.VALIDATION_MESSAGE_EMPTY_POSTCODE);
    commonPage.findElementWithUiPath(FindCouncilPage.POSTCODE_INPUT).clear();
    commonPage.findElementWithUiPath(FindCouncilPage.POSTCODE_INPUT).sendKeys("invalid");
    commonSteps.iVerifyValidationMessage(FindCouncilPage.VALIDATION_MESSAGE_INVALID_POSTCODE);
    commonPage.findElementWithUiPath(FindCouncilPage.POSTCODE_INPUT).clear();
    commonPage.findElementWithUiPath(FindCouncilPage.POSTCODE_INPUT).sendKeys(postcode);
    commonSteps.iClickOnContinueButton();
  }

  private String findPostcode(String country, boolean usingThirdParty) {
    if (usingThirdParty) {
      String postcode = "SE1 2QH"; // London borough of Southwark
      if ("scotland".equalsIgnoreCase(country)) {
        postcode = "EH8 8BG"; // City of Edinburgh council
      } else if ("wales".equalsIgnoreCase(country)) {
        postcode = "CF10 4UW"; // City of Cardiff council
      }
      return postcode;
    } else {
      String postcode = "FY1 1NA";
      if ("scotland".equalsIgnoreCase(country)) {
        postcode = "AB42 2BB";
      } else if ("wales".equalsIgnoreCase(country)) {
        postcode = "LL77 7TW";
      }
      return postcode;
    }
  }

  public void verifyPageContent(String applicant) {

    commonSteps.iShouldSeeTheCorrectURL(FindCouncilPage.PAGE_URL);

    if ("yourself".equals(applicant.toLowerCase())) {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(FindCouncilPage.PAGE_TITLE_YOURSELF);
      commonSteps.iShouldSeeTheHeading(FindCouncilPage.PAGE_TITLE_YOURSELF);
      commonSteps.thenIShouldSeeTheContent(FindCouncilPage.PAGE_LABEL_1_YOURSELF);
    } else {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(FindCouncilPage.PAGE_TITLE_SOMEONE_ELSE);
      commonSteps.iShouldSeeTheHeading(FindCouncilPage.PAGE_TITLE_SOMEONE_ELSE);
      commonSteps.thenIShouldSeeTheContent(FindCouncilPage.PAGE_LABEL_1_SOMEONE_ELSE);
    }
  }
}
