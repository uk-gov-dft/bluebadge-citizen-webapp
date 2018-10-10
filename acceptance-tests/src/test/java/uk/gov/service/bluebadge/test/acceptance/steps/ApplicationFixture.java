package uk.gov.service.bluebadge.test.acceptance.steps;

import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.Contact.EMAIL_ADDRESS;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.Contact.FULL_NAME;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.Contact.PRIMARY_CONTACT_NUMBER;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.Contact.SECONDARY_CONTACT_NUMBER;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.EleCheck.HAS_RECEIVED_DLA;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.EleCheck.MAIN_REASON_LIST;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.EleCheck.NEVER_RECEIVED_DLA;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.EleCheck.PLACE_CAN_WALK;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.EleCheck.TIME_TO_DESTINATION;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.Person.DOB_DAY;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.Person.DOB_MONTH;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.Person.DOB_YEAR;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.Person.GENDER_FEMALE;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.Person.GENDER_MALE;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.Person.GENDER_UNSPECIFIED;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.Preamble.COUNCIL_INPUT;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.Walkd.ADD_MOBILITY_BUTTON;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.Walkd.AID_TYPE_WHEELCHAIR;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.Walkd.PROVIDED_CODE_PRESCRIBE;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.Walkd.USAGE;

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
    clearAndSendKeys(Ids.Person.NAME, "Test Username");
    sitePage.findPageElementById("hasBirthName.no").click();
    pressContinue();
  }

  @And("^I complete date of birth page for \"(CHILD|ADULT)\"")
  public void iCompleteDateOfBirthPage(String age_category) {
    Calendar now = Calendar.getInstance();
    int dob_year = 1900;

    if (age_category.equals("CHILD")) dob_year = now.get(Calendar.YEAR) - 10;
    else dob_year = now.get(Calendar.YEAR) - 30;

    clearAndSendKeys(DOB_DAY, "1");
    clearAndSendKeys(DOB_MONTH, "1");
    clearAndSendKeys(DOB_YEAR, Integer.toString(dob_year));
    pressContinue();
  }

  @And("^I complete eligible page$")
  public void iCompleteEligiblePage() {
    sitePage.findElementWithText("Start application").click();
  }

  @And("^I complete gender page for \"(Boy|Girl|Man|Woman|Identify in a different way)\"")
  public void iCompleteGenderPageFor(String gender) {
    if (gender.equals("Boy") || gender.equals("Man"))
      sitePage.findPageElementById(GENDER_MALE).click();
    else if (gender.equals("Girl") || gender.equals("Woman"))
      sitePage.findPageElementById(GENDER_FEMALE).click();
    else sitePage.findPageElementById(GENDER_UNSPECIFIED).click();

    pressContinue();
  }

  @And("^I complete describe health conditions page$")
  public void iCompleteDescribeHealthConditionsPage() throws Throwable {
    clearAndSendKeys("descriptionOfConditions", "Sample health condition");
    pressContinue();
  }

  @And("^I complete declaration page$")
  public void iCompleteDeclarationPage() {
    sitePage.findPageElementById("agreed").click();
    pressContinue();
  }

  @And("^I complete planning points page for \"(12|10|8|4|0)\"")
  public void iCompletePlanningPointsPageFor(String points) {
    sitePage.findPageElementById(Ids.EleCheck.PLANNING_POINTS + "_" + points).click();
    pressContinue();
  }

  @And("^I complete what makes walking difficult page for \"(HELP|PAIN|DANGEROUS|NONE)\"$")
  public void iCompleteWhatMakesWalkingDifficultPageFor(String difficulty) {
    sitePage.findPageElementById(Ids.EleCheck.WALKING_DIFFICULTY_LIST + "." + difficulty).click();
    pressContinue();
  }

  @And("^I complete dla allowance page for \"(YES|NO)\"$")
  public void iCompleteDlaAllowancePageFor(String option) {
    if ("YES".equals(option)) sitePage.findPageElementById(HAS_RECEIVED_DLA).click();
    else sitePage.findPageElementById(NEVER_RECEIVED_DLA).click();
    pressContinue();
  }

  @And("I complete contact page for \"(yourself|someone else)\"")
  public void iCompleteContactPage(String myselfOrOther) {
    if ("someone else".equalsIgnoreCase(myselfOrOther)) {
      clearAndSendKeys(FULL_NAME, "Some Contact");
    }

    clearAndSendKeys(PRIMARY_CONTACT_NUMBER, "01270848484");
    clearAndSendKeys(SECONDARY_CONTACT_NUMBER, "01270848400");
    clearAndSendKeys(EMAIL_ADDRESS, "some@contact.com");

    pressContinue();
  }

  @And("^I complete address page$")
  public void iCompleteAddressPage() throws Throwable {
    clearAndSendKeys("buildingAndStreet", "120");
    clearAndSendKeys("optionalAddress", "London Road");
    clearAndSendKeys("townOrCity", "Manchester");
    clearAndSendKeys("postcode", "M4 1FS");

    pressContinue();
  }

  @And("^I complete NI number page$")
  public void iCompleteNINumberPage() throws Throwable {
    clearAndSendKeys(Ids.Person.NI, "AB123456A");
    pressContinue();
  }

  @And("^I complete NI number page without a NI$")
  public void iCompleteNINumberPageWithoutNI() {
    sitePage.findElementWithText(Ids.Person.NO_NI_TEXT).click();
    sitePage.findElementWithText(Ids.Person.NO_NI_LINK_TEXT).click();
    pressContinue();
  }

  @And("^I complete where can you walk page$")
  public void iCompleteWhereCanYouWalkPage() throws Throwable {
    clearAndSendKeys(PLACE_CAN_WALK, "to the Post office on the High Street");
    clearAndSendKeys(TIME_TO_DESTINATION, "10 minutes");
    pressContinue();
  }

  @And("^I complete lump sum of the AFRFCS Scheme page for \"(YES|NO)\"$")
  public void iCompleteLumpSumToOfTheAFRFCSSchemePageFor(String option) {
    sitePage
        .findPageElementById(Ids.EleCheck.RECEIVED_COMPENSATION + "." + option.toLowerCase())
        .click();
    pressContinue();
  }

  @And("^I complete have permanent disability page for \"(YES|NO)\"$")
  public void iCompleteHavePermanentDisabilityDisabilityPageFor(String option) {
    sitePage.findPageElementById(Ids.EleCheck.HAS_DISABILITY + "." + option.toLowerCase()).click();
    pressContinue();
  }

  @And("^I complete has mental disorder page for \"(YES|NO)\"$")
  public void iCompleteHasMentalDisorderPageFor(String option) {
    sitePage
        .findPageElementById(Ids.EleCheck.HAS_MENTAL_DISORDER + "." + option.toLowerCase())
        .click();
    pressContinue();
  }

  @And("^I complete has mobility component page for \"(YES|NO)\"$")
  public void iCompleteHasMobilityComponentPage(String option) {
    if ("YES".equals(option))
      sitePage
          .findPageElementById(Ids.EleCheck.AWARDED_HIGHER_RATE_MOBILITY + "." + "true")
          .click();
    else
      sitePage
          .findPageElementById(Ids.EleCheck.AWARDED_HIGHER_RATE_MOBILITY + "." + "false")
          .click();
    pressContinue();
  }

  @And("^I complete the what makes walking difficult page$")
  public void iCompleteTheWhatMakesWalkingDifficultPage() {
    sitePage.findPageElementById("whatWalkingDifficulties1").click();
    pressContinue();
  }

  public void clearAndSendKeys(String element, String value) {
    sitePage.findPageElementById(element).clear();
    sitePage.findPageElementById(element).sendKeys(value);
  }

  @And("^I complete the mobility aids page for \"(YES|NO)\"$")
  public void iCompleteTheMobilityAidsPage(String option) {
    sitePage.findPageElementById(Ids.Walkd.MOBILITY_AID_OPTION + option.toLowerCase()).click();

    if ("YES".equals(option)) {
      // Needs to update this to use id or data-uipath
      sitePage.findElementAddMobilityAid().click();
      sitePage.findPageElementById(AID_TYPE_WHEELCHAIR).click();
      clearAndSendKeys(USAGE, "All the time");
      sitePage.findPageElementById(PROVIDED_CODE_PRESCRIBE).click();
      sitePage.findElementWithUiPath(ADD_MOBILITY_BUTTON).click();
    }
    pressContinue();
  }

  @And("^I complete the treatments page for \"(YES|NO)\"$")
  public void iCompleteTheTreatmentsPage(String option) {
    sitePage.findPageElementById(Ids.Walkd.HAS_TREATMENT_OPTION + option.toLowerCase()).click();

    pressContinue();
  }
}
