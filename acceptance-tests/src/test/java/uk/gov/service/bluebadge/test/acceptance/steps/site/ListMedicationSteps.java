package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.ListMedicationPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

import java.util.ArrayList;
import java.util.List;

public class ListMedicationSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;

  private AddMedicationSteps addMedicationSteps;

  @Autowired
  public ListMedicationSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
    this.addMedicationSteps = new AddMedicationSteps(commonPage, commonSteps);
  }

  @And(
      "^I validate the medication page for a \"(yourself|someone else)\" application for \"(YES|NO)\"$")
  public void iValidateTheMedicationPage(String applicant, String option) {
    verifyPageContent(applicant);
    validateMandatoryFields();

    if ("YES".equals(option)) {
      commonPage.selectRadioButton(ListMedicationPage.TREATMENT_YES_OPTION);
      commonPage.findPageElementById(ListMedicationPage.TREATMENT_YES_OPTION).click();
      commonPage.findPageElementById(ListMedicationPage.ADD_FIRST_MEDICATION).click();
      addMedicationSteps.iValidateAddMedicationPage();
    } else {
      commonPage.selectRadioButton(ListMedicationPage.TREATMENT_NO_OPTION);
    }
    commonSteps.iClickOnContinueButton();
  }

  public void verifyPageContent(String applicant) {

    commonSteps.iShouldSeeTheCorrectURL(ListMedicationPage.PAGE_URL);

    if ("yourself".equals(applicant.toLowerCase())) {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(ListMedicationPage.PAGE_TITLE);
      commonSteps.iShouldSeeTheHeading(ListMedicationPage.HEADER);
    } else {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(
          ListMedicationPage.PAGE_TITLE_SOMEONE_ELSE);
      commonSteps.iShouldSeeTheHeading(ListMedicationPage.HEADER_SOMEONE_ELSE);
    }
  }

  private void validateMandatoryFields() {
    List<String> messages = new ArrayList<>();
    messages.add(ListMedicationPage.VALIDATION_MESSAGE_FOR_NO_OPTION);
    commonSteps.iVerifyMultipleValidationMessages(messages);
  }

}
