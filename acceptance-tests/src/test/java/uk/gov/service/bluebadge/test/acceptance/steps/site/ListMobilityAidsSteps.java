package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.And;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.ListMobilityAidsPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class ListMobilityAidsSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;

  private AddMobilityAidsSteps addMobilityAidsSteps;

  @Autowired
  public ListMobilityAidsSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
    this.addMobilityAidsSteps = new AddMobilityAidsSteps(commonPage, commonSteps);
  }

  @And(
      "^I validate the mobility aids page for a \"(yourself|someone else)\" application for \"(YES|NO)\"$")
  public void iValidateListMobilityAidsPage(String applicant, String option) {

    verifyPageContent(applicant);
    validateMandatoryFields();

    if ("YES".equals(option)) {
      commonPage.selectRadioButton(ListMobilityAidsPage.MOBILITY_AID_OPTION);
      commonPage.findElementAddMobilityAid().click();
      addMobilityAidsSteps.iValidateAddMobilityAidsPage();
    } else {
      commonPage.selectRadioButton(ListMobilityAidsPage.MOBILITY_AID_OPTION + option.toLowerCase());
    }
    commonSteps.iClickOnContinueButton();
  }

  public void verifyPageContent(String applicant) {

    commonSteps.iShouldSeeTheCorrectURL(ListMobilityAidsPage.PAGE_URL);

    if ("yourself".equals(applicant.toLowerCase())) {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(ListMobilityAidsPage.PAGE_TITLE);
      commonSteps.iShouldSeeTheHeading(ListMobilityAidsPage.HEADER);
    } else {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(
          ListMobilityAidsPage.PAGE_TITLE_SOMEONE_ELSE);
      commonSteps.iShouldSeeTheHeading(ListMobilityAidsPage.HEADER_SOMEONE_ELSE);
    }
  }

  private void validateMandatoryFields() {
    List<String> messages = new ArrayList<>();
    messages.add(ListMobilityAidsPage.VALIDATION_MESSAGE_FOR_NO_OPTION);
    commonSteps.iVerifyMultipleValidationMessages(messages);
  }

  @And("^I complete the mobility aids page for \"(YES|NO)\"$")
  public void iCompleteTheMobilityAidsPage(String option) {
    if ("YES".equals(option)) {
      commonPage.selectRadioButton(ListMobilityAidsPage.MOBILITY_AID_OPTION);
      // Needs to update this to use id or data-uipath
      commonPage.findElementAddMobilityAid().click();
      addMobilityAidsSteps.iCompleteAddMobilityAidsPage();
    } else {
      commonPage.selectRadioButton(ListMobilityAidsPage.MOBILITY_AID_OPTION + option.toLowerCase());
    }
    commonSteps.iClickOnContinueButton();
  }
}
