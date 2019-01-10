package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.And;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.SupportingDocumentsPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.ApplicationFixture;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class SupportingDocumentsSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;
  private ApplicationFixture applicationFixture;

  @Autowired
  public SupportingDocumentsSteps(
      CommonPage commonPage, CommonSteps commonSteps, ApplicationFixture applicationFixture) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
    this.applicationFixture = applicationFixture;
  }

  @And(
      "^I validate the supporting documents page for a \"(yourself|someone else)\" application for \"(YES|NO)\"$")
  public void iValidateTheSupportingDocumentsPage(String applicant, String option) {
    verifyPageContent(applicant);
    validateMandatoryFields();

    if ("YES".equals(option)) {
      commonPage.selectRadioButton(SupportingDocumentsPage.SUPPORTING_DOCUMENTS_YES_OPTION);
      commonPage
          .findPageElementById(SupportingDocumentsPage.SUPPORTING_DOCUMENTS_YES_OPTION)
          .click();
      commonSteps.iClickOnContinueButton();
      List<String> messages = new ArrayList<>();
      messages.add(SupportingDocumentsPage.VALIDATION_MESSAGE_FOR_YES_BUT_NO_UPLOAD);
      //The below code is for validating mmessage for invalid filetype which needs fixing
      //      commonSteps.iVerifyMultipleValidationMessages(messages);
      //      applicationFixture.iCompleteUploadSupportingDocumentPageWithADocument("INVALID");
      //      messages.clear();
      //      messages.add(SupportingDocumentsPage.VALIDATION_MESSAGE_FOR_INVALID_UPLOAD);
      //      commonSteps.iVerifyMultipleValidationMessages(messages);
      applicationFixture.iCompleteUploadSupportingDocumentPageWithADocument("GIF");

    } else {
      commonPage.selectRadioButton(SupportingDocumentsPage.SUPPORTING_DOCUMENTS_NO_OPTION);
      commonSteps.iClickOnContinueButton();
    }
  }

  public void verifyPageContent(String applicant) {

    commonSteps.iShouldSeeTheCorrectURL(SupportingDocumentsPage.PAGE_URL);

    if ("yourself".equals(applicant.toLowerCase())) {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(SupportingDocumentsPage.PAGE_TITLE);
      commonSteps.iShouldSeeTheHeading(SupportingDocumentsPage.HEADER);
    } else {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(
          SupportingDocumentsPage.PAGE_TITLE_SOMEONE_ELSE);
      commonSteps.iShouldSeeTheHeading(SupportingDocumentsPage.HEADER_SOMEONE_ELSE);
    }
  }

  private void validateMandatoryFields() {
    List<String> messages = new ArrayList<>();
    messages.add(SupportingDocumentsPage.VALIDATION_MESSAGE_FOR_NO_OPTION);
    commonSteps.iVerifyMultipleValidationMessages(messages);
  }
}
