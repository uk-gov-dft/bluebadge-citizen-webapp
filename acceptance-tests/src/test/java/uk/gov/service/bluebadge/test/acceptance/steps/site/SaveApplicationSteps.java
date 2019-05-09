package uk.gov.service.bluebadge.test.acceptance.steps.site;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
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
    verifyPageContent(applicant);

    //To validate Empty EMAIL {This is broken till we do the next story to do the save application page properly}
    //commonSteps.iVerifyValidationMessage(SaveApplicationPage.VALIDATION_MESSAGE_FOR_EMPTY_EMAIL);

    //Enter valid values and move on
    commonPage.findElementWithUiPath(SaveApplicationPage.EMAIL).clear();
    commonPage.findElementWithUiPath(SaveApplicationPage.EMAIL).sendKeys(SaveApplicationPage.VALID_EMAIL);

    verifyThePostcodeHasAutoFilled();

    commonSteps.iClickOnContinueButton();
  }

  public void verifyPageContent(String applicant) {

    commonSteps.iShouldSeeTheCorrectURL(SaveApplicationPage.PAGE_URL);

    if ("yourself".equals(applicant.toLowerCase())) {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(SaveApplicationPage.PAGE_TITLE_YOURSELF);
      commonSteps.iShouldSeeTheHeading(SaveApplicationPage.PAGE_TITLE_YOURSELF);
    } else {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(SaveApplicationPage.PAGE_TITLE_SOMEONE_ELSE);
      commonSteps.iShouldSeeTheHeading(SaveApplicationPage.PAGE_TITLE_SOMEONE_ELSE);
    }
  }

  private void verifyThePostcodeHasAutoFilled() {
    assertThat(
            "Previously enterred postcode should be auto filled",
            commonPage.findElementWithUiPath(SaveApplicationPage.POSTCODE).getAttribute("value"),
            is("M4 1FS"));
  }

  @And("^I validate return to saved application page for a \"([^\"]*)\" application$")
  public void iValidateReturnToSavedApplicationPageForAApplication(String arg0) throws Throwable {
    // Write code here that turns the phrase above into concrete actions
    throw new PendingException();
  }
}
