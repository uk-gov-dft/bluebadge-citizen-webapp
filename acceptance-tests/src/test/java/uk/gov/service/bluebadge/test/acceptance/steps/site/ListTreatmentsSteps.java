package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.And;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.ListTreatmentsPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class ListTreatmentsSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;

  private AddTreatmentsSteps addTreatmentsSteps;

  @Autowired
  public ListTreatmentsSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
    this.addTreatmentsSteps = new AddTreatmentsSteps(commonPage, commonSteps);
  }

  @And(
      "^I validate the treatments page for a \"(yourself|someone else)\" application for \"(YES|NO)\"$")
  public void iValidateTheTreatmentsPage(String applicant, String option) {
    verifyPageContent(applicant);
    validateMandatoryFields();

    if ("YES".equals(option)) {
      commonPage.selectRadioButton(ListTreatmentsPage.TREATMENT_YES_OPTION);
      commonPage.findPageElementById(ListTreatmentsPage.TREATMENT_YES_OPTION).click();
      commonPage.findPageElementById(ListTreatmentsPage.ADD_FIRST_TREATMENT).click();
      addTreatmentsSteps.iValidateAddTreatmentsPage();
    } else {
      commonPage.selectRadioButton(ListTreatmentsPage.TREATMENT_NO_OPTION);
    }
    commonSteps.iClickOnContinueButton();
  }

  public void verifyPageContent(String applicant) {

    commonSteps.iShouldSeeTheCorrectURL(ListTreatmentsPage.PAGE_URL);

    if ("yourself".equals(applicant.toLowerCase())) {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(ListTreatmentsPage.PAGE_TITLE);
      commonSteps.iShouldSeeTheHeading(ListTreatmentsPage.HEADER);
    } else {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(
          ListTreatmentsPage.PAGE_TITLE_SOMEONE_ELSE);
      commonSteps.iShouldSeeTheHeading(ListTreatmentsPage.HEADER_SOMEONE_ELSE);
    }
  }

  private void validateMandatoryFields() {
    List<String> messages = new ArrayList<>();
    messages.add(ListTreatmentsPage.VALIDATION_MESSAGE_FOR_NO_OPTION);
    commonSteps.iVerifyMultipleValidationMessages(messages);
  }

  //    @And("^I complete the treatments page for \"(YES|NO)\"$")
  //    public void iCompleteTheTreatmentsPage(String option) {
  //        if ("YES".equals(option)) {
  //            commonPage.selectRadioButton(ListTreatmentsPage.TREATMENT_YES_OPTION);
  //            // Needs to update this to use id or data-uipath
  //            commonPage.findPageElementById(ListTreatmentsPage.TREATMENT_YES_OPTION).click();
  //            addTreatmentsSteps.iCompleteAddMedicationPage();
  //        } else {
  //            commonPage.selectRadioButton(ListTreatmentsPage.TREATMENT_NO_OPTION);
  //        }
  //        commonSteps.iClickOnContinueButton();
  //    }

}
