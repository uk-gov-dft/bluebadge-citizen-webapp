package uk.gov.service.bluebadge.test.acceptance.steps.site;

import cucumber.api.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.ChooseCouncilPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.ApplicationFixture;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;

public class ChooseCouncilSteps extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private CommonPage commonPage;
  private String journeyOption;

  @Autowired
  public ChooseCouncilSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }

  @And(
      "^I validate choose council page for \"(yourself|someone else)\" and select a council in \"(england|wales|scotland)\"")
  public void iValidateChooseCouncilPageForAndSelectACouncil(String applicant, String country) {
    String council = chooseCouncil(country);
    verifyPageContent(applicant);
    commonSteps.iVerifyValidationMessage(ChooseCouncilPage.VALIDATION_MESSAGE_FOR_NO_OPTION);

    commonPage.findPageElementById(ChooseCouncilPage.COUNCIL_INPUT).sendKeys(council);
    commonPage.selectFromAutoCompleteList(ChooseCouncilPage.COUNCIL_INPUT, council);
    commonSteps.iClickOnContinueButton();
  }

  private String chooseCouncil(String country) {
    String fullCouncil = "Worcester city council";
    if ("scotland".equalsIgnoreCase(country)) {
      fullCouncil = "Aberdeenshire council";
    } else if ("wales".equalsIgnoreCase(country)) {
      fullCouncil = "Isle of Anglesey county council";
    }
    return fullCouncil;
  }

  public void verifyPageContent(String applicant) {

    commonSteps.iShouldSeeTheCorrectURL(ChooseCouncilPage.PAGE_URL);

    if ("yourself".equals(applicant.toLowerCase())) {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(ChooseCouncilPage.PAGE_TITLE_YOURSELF);
      commonSteps.iShouldSeeTheHeading(ChooseCouncilPage.PAGE_TITLE_YOURSELF);
      commonSteps.thenIShouldSeeTheContent(ChooseCouncilPage.PAGE_LABEL_1_YOURSELF);
    } else {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(
              ChooseCouncilPage.PAGE_TITLE_SOMEONE_ELSE);
      commonSteps.iShouldSeeTheHeading(ChooseCouncilPage.PAGE_TITLE_SOMEONE_ELSE);
      commonSteps.thenIShouldSeeTheContent(ChooseCouncilPage.PAGE_LABEL_1_SOMEONE_ELSE);
    }
  }
}
