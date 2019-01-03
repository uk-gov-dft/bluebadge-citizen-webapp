package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.And;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.ListHealthcareProfessionalPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class ListHealthcareProfessionalSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;

  private AddHealthcareProfessionalSteps addHealthcareProfessionalSteps;

  @Autowired
  public ListHealthcareProfessionalSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
    this.addHealthcareProfessionalSteps =
        new AddHealthcareProfessionalSteps(commonPage, commonSteps);
  }

  @And(
      "^I validate the healthcare professional page for a \"(yourself|someone else)\" application for \"(YES|NO)\"$")
  public void iValidateTheHealthcareProfessionalsPage(String applicant, String option) {
    verifyPageContent(applicant);
    validateMandatoryFields();

    if ("YES".equals(option)) {
      commonPage.selectRadioButton(
          ListHealthcareProfessionalPage.HEALTHCARE_PROFESSIONAL_YES_OPTION);
      commonPage
          .findPageElementById(ListHealthcareProfessionalPage.HEALTHCARE_PROFESSIONAL_YES_OPTION)
          .click();
      commonPage
          .findPageElementById(ListHealthcareProfessionalPage.ADD_FIRST_HEALTHCARE_PROFESSIONAL)
          .click();
      addHealthcareProfessionalSteps.iValidateAddHealthcareProfessionalPage();
    } else {
      commonPage.selectRadioButton(
          ListHealthcareProfessionalPage.HEALTHCARE_PROFESSIONAL_NO_OPTION);
    }
    commonSteps.iClickOnContinueButton();
  }

  public void verifyPageContent(String applicant) {

    commonSteps.iShouldSeeTheCorrectURL(ListHealthcareProfessionalPage.PAGE_URL);

    if ("yourself".equals(applicant.toLowerCase())) {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(
          ListHealthcareProfessionalPage.PAGE_TITLE);
      commonSteps.iShouldSeeTheHeading(ListHealthcareProfessionalPage.HEADER);
    } else {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(
          ListHealthcareProfessionalPage.PAGE_TITLE_SOMEONE_ELSE);
      commonSteps.iShouldSeeTheHeading(ListHealthcareProfessionalPage.HEADER_SOMEONE_ELSE);
    }
  }

  private void validateMandatoryFields() {
    List<String> messages = new ArrayList<>();
    messages.add(ListHealthcareProfessionalPage.VALIDATION_MESSAGE_FOR_NO_SELECTION);
    commonSteps.iVerifyMultipleValidationMessages(messages);
  }
}
