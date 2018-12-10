package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.DOBPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class DOBSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;

  @Autowired
  public DOBSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }

  @And("^I validate date of birth page for a \"(yourself|someone else)\" application$")
  public void iValidateDateOfBirthPageForAApplication(String applicant) {
    verifyPageContent(applicant);

    //To validate Empty DOB
    commonSteps.iVerifyValidationMessage(DOBPage.VALIDATION_MESSAGE_FOR_EMPTY_DOB);

    //To validate invalid Day
    commonPage.findElementWithUiPath("dateOfBirth.day.field").sendKeys("40");
    commonPage.findElementWithUiPath("dateOfBirth.month.field").sendKeys("01");
    commonPage.findElementWithUiPath("dateOfBirth.year.field").sendKeys("1985");
    commonSteps.iVerifyValidationMessage(DOBPage.VALIDATION_MESSAGE_FOR_INVALID_DOB);

    //To validate invalid month
    commonPage.findElementWithUiPath("dateOfBirth.day.field").clear();
    commonPage.findElementWithUiPath("dateOfBirth.month.field").clear();
    commonPage.findElementWithUiPath("dateOfBirth.year.field").clear();

    commonPage.findElementWithUiPath("dateOfBirth.day.field").sendKeys("01");
    commonPage.findElementWithUiPath("dateOfBirth.month.field").sendKeys("13");
    commonPage.findElementWithUiPath("dateOfBirth.year.field").sendKeys("1985");
    commonSteps.iVerifyValidationMessage(DOBPage.VALIDATION_MESSAGE_FOR_INVALID_DOB);

    //To validate invalid year
    commonPage.findElementWithUiPath("dateOfBirth.day.field").clear();
    commonPage.findElementWithUiPath("dateOfBirth.month.field").clear();
    commonPage.findElementWithUiPath("dateOfBirth.year.field").clear();

    commonPage.findElementWithUiPath("dateOfBirth.day.field").sendKeys("01");
    commonPage.findElementWithUiPath("dateOfBirth.month.field").sendKeys("01");
    commonPage.findElementWithUiPath("dateOfBirth.year.field").sendKeys("19851");
    commonSteps.iVerifyValidationMessage(DOBPage.VALIDATION_MESSAGE_FOR_INVALID_DOB);

    //To validate DOB that is in future
    commonPage.findElementWithUiPath("dateOfBirth.day.field").clear();
    commonPage.findElementWithUiPath("dateOfBirth.month.field").clear();
    commonPage.findElementWithUiPath("dateOfBirth.year.field").clear();

    commonPage.findElementWithUiPath("dateOfBirth.day.field").sendKeys("01");
    commonPage.findElementWithUiPath("dateOfBirth.month.field").sendKeys("01");
    commonPage.findElementWithUiPath("dateOfBirth.year.field").sendKeys("2099");
    commonSteps.iVerifyValidationMessage(DOBPage.VALIDATION_MESSAGE_FOR_DOB_IN_FUTURE);

    //Enter valid values and move on
    commonPage.findElementWithUiPath("dateOfBirth.day.field").clear();
    commonPage.findElementWithUiPath("dateOfBirth.month.field").clear();
    commonPage.findElementWithUiPath("dateOfBirth.year.field").clear();

    commonPage.findElementWithUiPath("dateOfBirth.day.field").sendKeys("01");
    commonPage.findElementWithUiPath("dateOfBirth.month.field").sendKeys("01");
    commonPage.findElementWithUiPath("dateOfBirth.year.field").sendKeys("1985");

    commonSteps.iClickOnContinueButton();
  }

  public void verifyPageContent(String applicant) {

    commonSteps.iShouldSeeTheCorrectURL(DOBPage.PAGE_URL);

    if ("yourself".equals(applicant.toLowerCase())) {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(DOBPage.PAGE_TITLE_YOURSELF);
      commonSteps.iShouldSeeTheHeading(DOBPage.PAGE_HEADING);
    } else {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(DOBPage.PAGE_TITLE_SOMEONE_ELSE);
      commonSteps.iShouldSeeTheHeading(DOBPage.PAGE_HEADING_SOMEONE_ELSE);
    }
  }
}
