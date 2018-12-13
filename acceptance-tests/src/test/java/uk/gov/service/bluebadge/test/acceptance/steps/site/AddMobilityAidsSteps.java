package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.And;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.AddMobilityAidsPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class AddMobilityAidsSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;

  @Autowired
  public AddMobilityAidsSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }

  @And("^I validate the add mobility aids page \"$")
  public void iValidateAddMobilityAidsPage() {

    verifyPageContent();
    validateMandatoryFields();

    iCompleteAddMobilityAidsPage();
  }

  public void verifyPageContent() {

    commonSteps.iShouldSeeTheCorrectURL(AddMobilityAidsPage.PAGE_URL);
    commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(AddMobilityAidsPage.PAGE_TITLE);
  }

  private void validateMandatoryFields() {
    List<String> messages = new ArrayList<>();
    messages.add(AddMobilityAidsPage.VALIDATION_MESSAGE_FOR_NO_TYPE);
    messages.add(AddMobilityAidsPage.VALIDATION_MESSAGE_FOR_NO_HOW_USED);
    messages.add(AddMobilityAidsPage.VALIDATION_MESSAGE_FOR_NO_HOW_PROVIDED);
    commonSteps.iVerifyMultipleValidationMessagesInChildPages(
        messages, AddMobilityAidsPage.MOBILITY_AID_ADD_CONFIRM_BUTTON);
  }

  @And("^I complete add mobility aids page \"$")
  public void iCompleteAddMobilityAidsPage() {
    commonPage.clearAndSendKeys(AddMobilityAidsPage.MOBILITY_AID_TYPE, "Wheel chair");
    commonPage.clearAndSendKeys(AddMobilityAidsPage.MOBILITY_AID_ADD_USAGE, "All the time");
    commonPage.selectRadioButton(AddMobilityAidsPage.MOBILITY_AID_ADD_PROVIDED_CODE_PRESCRIBE);
    commonSteps.iClickOnAddButtonOnChildPage(AddMobilityAidsPage.MOBILITY_AID_ADD_CONFIRM_BUTTON);
  }
}
