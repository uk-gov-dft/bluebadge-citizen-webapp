package uk.gov.service.bluebadge.test.acceptance.steps.site;


import cucumber.api.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.ChooseCouncilPage;
import uk.gov.service.bluebadge.test.acceptance.pages.site.CommonPage;
import uk.gov.service.bluebadge.test.acceptance.steps.AbstractSpringSteps;
import uk.gov.service.bluebadge.test.acceptance.steps.ApplicationFixture;
import uk.gov.service.bluebadge.test.acceptance.steps.CommonSteps;


public class ChooseCouncilSteps  extends AbstractSpringSteps {

  private CommonSteps commonSteps;
  private ChooseCouncilPage chooseCouncilPage;
  private CommonPage commonPage;
  private ApplicationFixture applicationFixture;
  private String journeyOption;

  @Autowired
  public ChooseCouncilSteps(CommonPage commonPage, CommonSteps commonSteps) {
    this.commonPage = commonPage;
    this.commonSteps = commonSteps;
  }

  @And("^I validate choose council page for \"(yourself|someone else)\" and select a council in \"(england|wales|scotland)\"")
  public void iValidateChooseCouncilPageForAndSelectACouncil(String applicant, String country) throws Throwable {
    String council = chooseCouncil(country);
    verifyPageContent(applicant);

    commonSteps.iClickOnContinueButton();
    commonSteps.andIshouldSeeErrorSummaryBox();
    commonSteps.iShouldSeeTextOnPage(chooseCouncilPage.VALIDATION_MESSAGE_FOR_NO_OPTION);
    commonPage.findPageElementById(chooseCouncilPage.COUNCIL_INPUT).sendKeys(council);
    commonPage.selectFromAutoCompleteList(chooseCouncilPage.COUNCIL_INPUT, council);
    commonSteps.iClickOnContinueButton();
  }

  public String chooseCouncil(String country){
    String council = "Worcester";
    String fullCouncil = "Worcester city council";
    if ("scotland".equalsIgnoreCase(country)) {
      council = "Aberdeenshire";
      fullCouncil = "Aberdeenshire council";
    } else if ("wales".equalsIgnoreCase(country)) {
      council = "Anglesey";
      fullCouncil = "Isle of Anglesey county council";
    }
    return fullCouncil;
  }

  public void verifyPageContent(String applicant){
    if("yourself".equals(applicant.toLowerCase())) {
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(chooseCouncilPage.PAGE_TITLE_YOURSELF);
      commonSteps.iShouldSeeTheHeading(chooseCouncilPage.PAGE_TITLE_YOURSELF);
      commonSteps.thenIShouldSeeTheContent(chooseCouncilPage.PAGE_LABEL_1_YOURSELF);
    }
    else{
      commonSteps.thenIShouldSeePageTitledWithGovUkSuffix(chooseCouncilPage.PAGE_TITLE_SOMEONE_ELSE);
      commonSteps.iShouldSeeTheHeading(chooseCouncilPage.PAGE_TITLE_SOMEONE_ELSE);
      commonSteps.thenIShouldSeeTheContent(chooseCouncilPage.PAGE_LABEL_1_SOMEONE_ELSE);
    }

  }
}
