package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.And;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.NamePage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class NameSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;

  @Autowired
  public NameSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }

  @And("^I validate name page for a \"(yourself|someone else)\" application$")
  public void iValidateNamePageForAApplication(String applicant) {
    verifyPageContent(applicant);
    //To validate when no data in entered and Continue button is clicked
    List<String> messages = new ArrayList<>();
    messages.add(NamePage.VALIDATION_MESSAGE_FOR_EMPTY_FULL_NAME);
    messages.add(NamePage.VALIDATION_MESSAGE_FOR_BIRTH_NAME_NO_OPTION);
    commonSteps.iVerifyMultipleValidationMessages(messages);

    //To validate am invalid name
    commonPage.findPageElementById(NamePage.NAME_BIRTH_NAME_OPTIONS).click();
    commonPage.findElementWithUiPath("fullName.field").sendKeys("23623548");
    commonSteps.iVerifyValidationMessage(NamePage.VALIDATION_MESSAGE_FOR_INVALID_FULL_NAME);

    //To validate Empty Birth Name
    commonPage.findElementWithUiPath("fullName.field").clear();
    commonPage.findElementWithUiPath("fullName.field").sendKeys("Walking Applicant");
    commonPage.findPageElementById(NamePage.NAME_BIRTH_NAME_OPTIONS + "." + "yes").click();
    commonSteps.iVerifyValidationMessage(NamePage.VALIDATION_MESSAGE_FOR_EMPTY_BIRTH_NAME);

    //To validate an invalid Birth Name
    commonPage.findElementWithUiPath("birthName.field").clear();
    commonPage.findElementWithUiPath("birthName.field").sendKeys("322322");
    commonSteps.iVerifyValidationMessage(NamePage.VALIDATION_MESSAGE_FOR_INVALID_BIRTH_NAME);

    //Enter valid values and move on
    commonPage.findElementWithUiPath("birthName.field").clear();
    commonPage.findElementWithUiPath("birthName.field").sendKeys("Applicant Birth Name");
    commonSteps.iClickOnContinueButton();
  }

  public void verifyPageContent(String applicant) {

    commonSteps.iShouldSeeTheCorrectURL(NamePage.PAGE_URL);

    if ("yourself".equals(applicant.toLowerCase())) {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(NamePage.PAGE_TITLE_YOURSELF);
      commonSteps.iShouldSeeTheHeading(NamePage.PAGE_TITLE_YOURSELF);
    } else {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(NamePage.PAGE_TITLE_SOMEONE_ELSE);
      commonSteps.iShouldSeeTheHeading(NamePage.PAGE_TITLE_SOMEONE_ELSE);
    }
  }
}
