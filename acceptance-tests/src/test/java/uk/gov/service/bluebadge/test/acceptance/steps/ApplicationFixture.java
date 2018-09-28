package uk.gov.service.bluebadge.test.acceptance.steps;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.SitePage;

import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.EleCheck.MAIN_REASON_LIST;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.Preamble.COUNCIL_INPUT;

public class ApplicationFixture extends AbstractSpringSteps {

  private SitePage sitePage;

  @Autowired
  public ApplicationFixture(SitePage sitePage) {
    this.sitePage = sitePage;
  }

  private void pressContinue() {
    sitePage.findElementWithText("Continue").click();
  }

  @Given("I complete applicant page for \"(yourself|someone else)\"")
  public void iCompleteApplicantPage(String myselfOrOther) {
    sitePage.openByPageName("applicant");
    if (myselfOrOther.equalsIgnoreCase("yourself")) {
      sitePage.findPageElementById(Ids.Preamble.APPLICANT_TYPE_YOURSELF_OPTION).click();
    } else if (myselfOrOther.equalsIgnoreCase("someone else")) {
      sitePage.findPageElementById(Ids.Preamble.APPLICANT_TYPE_SOMELSE_OPTION).click();
    }
    pressContinue();
  }

  @And("I complete select council page")
  public void iCompleteSelectCouncilPage() {
    iCompleteSelectCouncilPage("england");
  }

  @And("I complete select council page for \"(england|wales|scotland)\"")
  public void iCompleteSelectCouncilPage(String country) {
    String council = "Worcester";
    String fullCouncil = "Worcester city council";
    if ("scotland".equalsIgnoreCase(country)) {
      council = "Aberdeenshire";
      fullCouncil = "Aberdeenshire council";
    } else if ("wales".equalsIgnoreCase(country)) {
      council = "Anglesey";
      fullCouncil = "Isle of Anglesey county council";
    }

    sitePage.findPageElementById(COUNCIL_INPUT).sendKeys(council);
    sitePage.selectFromAutoCompleteList(COUNCIL_INPUT, fullCouncil);
    pressContinue();
  }

  @And("I complete your local authority page")
  public void iCompleteYourAuthorityPage() {
    pressContinue();
  }

  @And("I complete receive benefit page for \"(PIP|DLA|AFCS|WPMS|NONE)\"")
  public void iCompleteReceiveBenefitPageFor(String benefit) {
    sitePage.findPageElementById(Ids.EleCheck.BENEFIT_RECEIVED_LIST + "." + benefit).click();
    pressContinue();
  }

  @And("I complete main reason page for \"(TERMILL|CHILDBULK|CHILDVEHIC|WALKD|ARMS|BLIND)\"")
  public void iCompleteMainReasonPageFor(String benefit) {
    sitePage.findPageElementById(MAIN_REASON_LIST  + "." + benefit).click();
    pressContinue();
  }

  @And("I complete may be eligible page")
  public void iCompleteMayBeEligible(){
    sitePage.findElementWithText("Start application").click();
  }

  @And("I complete what's your name page")
  public void iCompleteWhatsYourNamePage(){
    sitePage.findPageElementById(Ids.Person.NAME).sendKeys("Test Username");
    sitePage.findPageElementById(Ids.Person.HAS_BIRTH_NAME_NO_OPTION).click();
    pressContinue();
  }
}
