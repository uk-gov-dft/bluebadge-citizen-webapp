package uk.gov.service.bluebadge.test.acceptance.steps.site;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import cucumber.api.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.EnterCodePage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.ReturnToApplicationPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.SaveApplicationPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class SaveApplicationSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;

  @Autowired
  public SaveApplicationSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }

  @And("^I validate save application page for a \"(yourself|someone else)\" application$")
  public void iValidateSaveApplicationPageForAApplication(String applicant) {
    verifySaveApplicationPageContent(applicant);

    //To validate Empty EMAIL {This is broken till we do the next story to do the save application page properly}
    //commonSteps.iVerifyValidationMessage(SaveApplicationPage.VALIDATION_MESSAGE_FOR_EMPTY_EMAIL);

    //Enter valid values and move on
    commonPage.findElementWithUiPath(SaveApplicationPage.EMAIL).clear();
    commonPage.findElementWithUiPath(SaveApplicationPage.EMAIL).sendKeys(SaveApplicationPage.VALID_EMAIL);

    verifyThePostcodeHasAutoFilledInSaveApplicationPage();

    commonSteps.iClickOnContinueButton();
  }

  public void verifySaveApplicationPageContent(String applicant) {

    commonSteps.iShouldSeeTheCorrectURL(SaveApplicationPage.PAGE_URL);

    if ("yourself".equals(applicant.toLowerCase())) {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(SaveApplicationPage.PAGE_TITLE_YOURSELF);
      commonSteps.iShouldSeeTheHeading(SaveApplicationPage.PAGE_TITLE_YOURSELF);
    } else {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(SaveApplicationPage.PAGE_TITLE_SOMEONE_ELSE);
      commonSteps.iShouldSeeTheHeading(SaveApplicationPage.PAGE_TITLE_SOMEONE_ELSE);
    }
  }

  private void verifyThePostcodeHasAutoFilledInSaveApplicationPage() {
    assertThat(
            "Previously enterred postcode should be auto filled",
            commonPage.findElementWithUiPath(SaveApplicationPage.POSTCODE).getAttribute("value"),
            is(SaveApplicationPage.VALID_POSTCODE));
  }


  @And("^I validate return to saved application page for a \"(yourself|someone else)\" application$")
  public void iValidateReturnToSavedApplicationPageForAApplication(String applicant) {
    VerifyReturnToApplicationPageContent(applicant);

    //To validate Empty EMAIL
    //commonSteps.iVerifyValidationMessage(ReturnToApplicationPage.VALIDATION_MESSAGE_FOR_EMPTY_EMAIL);

    //Enter valid values and move on
    commonPage.findElementWithUiPath(ReturnToApplicationPage.EMAIL).clear();
    commonPage.findElementWithUiPath(ReturnToApplicationPage.EMAIL).sendKeys(SaveApplicationPage.VALID_EMAIL);

    commonSteps.iClickOnContinueButton();
  }


  public void VerifyReturnToApplicationPageContent(String applicant) {

    commonSteps.iShouldSeeTheCorrectURL(ReturnToApplicationPage.PAGE_URL);

    if ("yourself".equals(applicant.toLowerCase())) {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(ReturnToApplicationPage.PAGE_TITLE_YOURSELF);
      commonSteps.iShouldSeeTheHeading(ReturnToApplicationPage.PAGE_TITLE_YOURSELF);
    } else {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(ReturnToApplicationPage.PAGE_TITLE_SOMEONE_ELSE);
      commonSteps.iShouldSeeTheHeading(ReturnToApplicationPage.PAGE_TITLE_SOMEONE_ELSE);
    }
  }

  @And("^I validate enter the 4 digit code page for a \"(yourself|someone else)\" application$")
  public void iValidateEnterTheDigitCodePageForAApplication(String applicant) throws Throwable {
    VerifyEnterCodePageContent(applicant);

    //Enter valid values and move on
    commonPage.findElementWithUiPath(EnterCodePage.POSTCODE).clear();
    commonPage.findElementWithUiPath(EnterCodePage.POSTCODE).sendKeys(SaveApplicationPage.VALID_POSTCODE);

    commonPage.findElementWithUiPath(EnterCodePage.CODE).clear();
    //commonPage.findElementWithUiPath(EnterCodePage.POSTCODE).sendKeys(SaveApplicationPage.VALID_POSTCODE);


    commonSteps.iClickOnContinueButton();
  }

  public void VerifyEnterCodePageContent(String applicant) {

    commonSteps.iShouldSeeTheCorrectURL(EnterCodePage.PAGE_URL);

    if ("yourself".equals(applicant.toLowerCase())) {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(EnterCodePage.PAGE_TITLE_YOURSELF);
      commonSteps.iShouldSeeTheHeading(EnterCodePage.PAGE_TITLE_YOURSELF);
    } else {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(EnterCodePage.PAGE_TITLE_SOMEONE_ELSE);
      commonSteps.iShouldSeeTheHeading(EnterCodePage.PAGE_TITLE_SOMEONE_ELSE);
    }
  }
}
