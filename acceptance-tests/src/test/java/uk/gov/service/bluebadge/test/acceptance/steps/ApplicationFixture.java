package uk.gov.service.bluebadge.test.acceptance.steps;

import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.EleCheck.*;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.Person.DOB_DAY;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.Person.DOB_MONTH;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.Person.DOB_YEAR;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.Preamble.COUNCIL_INPUT;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import java.util.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.SitePage;

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

  @And("^I complete select council page$")
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

  @And("I complete receive benefit page for \"(PIP|DLA|AFRFCS|WPMS|NONE)\"")
  public void iCompleteReceiveBenefitPageFor(String benefit) {
    sitePage.findPageElementById(Ids.EleCheck.BENEFIT_RECEIVED_LIST + "." + benefit).click();
    pressContinue();
  }

  @And("I complete moving around points page for \"(12|10|8|4|0)\"")
  public void iCompleteMovingAroundPointsPageFor(String points) {
    sitePage.findPageElementById(Ids.EleCheck.MOVING_POINTS + "_" + points).click();
    pressContinue();
  }

  @And("I complete main reason page for \"(TERMILL|CHILDBULK|CHILDVEHIC|WALKD|ARMS|BLIND|NONE)\"")
  public void iCompleteMainReasonPageFor(String benefit) {
    sitePage.findPageElementById(MAIN_REASON_LIST + "." + benefit).click();
    pressContinue();
  }

  @And("I complete may be eligible page")
  public void iCompleteMayBeEligible() {
    sitePage.findElementWithText("Start application").click();
  }

  @And("I complete what's your name page")
  public void iCompleteWhatsYourNamePage() {
    sitePage.findPageElementById(Ids.Person.NAME).sendKeys("Test Username");
    sitePage.findPageElementById(Ids.Person.HAS_BIRTH_NAME_NO_OPTION).click();
    pressContinue();
  }

  @And("^I complete date of birth page for \"(CHILD|ADULT)\"")
  public void iCompleteDateOfBirthPage(String age_category) throws Throwable {
    Calendar now = Calendar.getInstance();
    int dob_year = 1900;

    if (age_category.equals("CHILD")) dob_year = now.get(Calendar.YEAR) - 10;
    else dob_year = now.get(Calendar.YEAR) - 30;

    sitePage.findPageElementById(DOB_DAY).sendKeys("1");
    sitePage.findPageElementById(DOB_MONTH).sendKeys("1");
    sitePage.findPageElementById(DOB_YEAR).sendKeys(Integer.toString(dob_year));
    pressContinue();
  }

  @And("^I complete eligible page$")
  public void iCompleteEligiblePage() throws Throwable {
    sitePage.findElementWithText("Start application").click();
  }

  @And("^I complete gender page for \"(Boy|Girl|Man|Woman|I identify in a different way)\"")
  public void iCompleteGenderPageFor(String gender) throws Throwable {
    if (gender.equals("Boy") || gender.equals("Man"))
      sitePage.findPageElementById(GENDER_MALE).click();
    else if (gender.equals("Girl") || gender.equals("Woman"))
      sitePage.findPageElementById(GENDER_FEMALE).click();
    else sitePage.findPageElementById(GENDER_UNSPECIFIED).click();

    pressContinue();
  }

  @And("^I complete describe health conditions page$")
  public void iCompleteDescribeHealthConditionsPage() throws Throwable {
    sitePage.findPageElementById("descriptionOfConditions").sendKeys("Sample health condition");
    pressContinue();
  }

  @And("^I complete declaration page$")
  public void iCompleteDeclarationPage() throws Throwable {
    sitePage.findPageElementById("agreed").click();
    pressContinue();
  }

  @And("^I complete planning points page for \"(12|10|8|4|0)\"")
  public void iCompletePlanningPointsPageFor(String points) throws Throwable {
    sitePage.findPageElementById(Ids.EleCheck.PLANNING_POINTS + "_" + points).click();
    pressContinue();
  }

  @And("^I complete what makes walking difficult page for \"(HELP|PAIN|DANGEROUS|NONE)\"$")
  public void iCompleteWhatMakesWalkingDifficultPageFor(String difficulty) throws Throwable {
    sitePage.findPageElementById(Ids.EleCheck.WALKING_DIFFICULTY_LIST + "." + difficulty).click();
    pressContinue();
  }

  @And("^I complete dla allowance page for \"(YES|NO)\"$")
  public void iCompleteDlaAllowancePageFor(String option) throws Throwable {
    if (option.equals("YES")) sitePage.findPageElementById(HAS_RECEIVED_DLA).click();
    else sitePage.findPageElementById(NEVER_RECEIVED_DLA).click();
    pressContinue();
  }

    @And("^I complete address page$")
    public void iCompleteAddressPage() throws Throwable {
      sitePage.findPageElementById("buildingAndStreet").sendKeys("120");
      sitePage.findPageElementById("optionalAddress").sendKeys("London Road");
      sitePage.findPageElementById("townOrCity").sendKeys("Manchester");
      sitePage.findPageElementById("postcode").sendKeys("M4 1FS");

      pressContinue();
    }

    @And("^I complete NI number page$")
    public void iCompleteNINumberPage() throws Throwable {
        sitePage.findPageElementById(NI).sendKeys("AB123456A");
        pressContinue();
    }

  @And("^I complete NI number page without a NI$")
  public void iCompleteNINumberPageWithoutNI() throws Throwable {
    sitePage.findElementWithText(NO_NI_TEXT).click();
    sitePage.findElementWithText(NO_NI_LINK_TEXT).click();
    pressContinue();
  }
}
