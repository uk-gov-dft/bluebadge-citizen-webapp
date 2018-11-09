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
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.Person.*;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.Walkd.MOBILITY_AID_ADD_CONFIRM_BUTTON;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.Walkd.MOBILITY_AID_ADD_PROVIDED_CODE_PRESCRIBE;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.Walkd.MOBILITY_AID_ADD_USAGE;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.Walkd.MOBILITY_AID_TYPE_WHEELCHAIR;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import java.util.Calendar;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.*;

public class ApplicationFixture extends AbstractSpringSteps {

  private CommonPage commonPage;
  private ApplicantPage applicantPage;
  private ChooseCouncilPage chooseCouncilPage;

  @Autowired
  public ApplicationFixture(CommonPage commonPage) {
    this.commonPage = commonPage;
  }

  private void pressContinue() {
    commonPage.findElementWithText("Continue").click();
  }

  @Given("I complete applicant page for \"(yourself|someone else|organisation)\"")
  public void iCompleteApplicantPage(String myselfOrOther) {
    commonPage.openByPageName("applicant");
    if (myselfOrOther.equalsIgnoreCase("yourself")) {
      commonPage.findPageElementById(applicantPage.APPLICANT_TYPE_OPTION_LIST).click();
    } else if (myselfOrOther.equalsIgnoreCase("someone else")) {
      commonPage.findPageElementById(applicantPage.APPLICANT_TYPE_SOMELSE_OPTION).click();
    } else if (myselfOrOther.equalsIgnoreCase("organisation")) {
      commonPage.findPageElementById(applicantPage.APPLICANT_TYPE_ORG_OPTION).click();
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

    commonPage.findPageElementById(chooseCouncilPage.COUNCIL_INPUT).sendKeys(council);
    commonPage.selectFromAutoCompleteList(chooseCouncilPage.COUNCIL_INPUT, fullCouncil);
    pressContinue();
  }

  @And("I complete your local authority page")
  public void iCompleteYourAuthorityPage() {
    pressContinue();
  }

  @And("I complete receive benefit page for \"(PIP|DLA|AFRFCS|WPMS|NONE)\"")
  public void iCompleteReceiveBenefitPageFor(String benefit) {
    if (benefit.equals("PIP")) {
      commonPage.findPageElementById(BenifitsPage.BENEFIT_RECEIVED_LIST).click();
    } else {
      commonPage.findPageElementById(BenifitsPage.BENEFIT_RECEIVED_LIST + "." + benefit).click();
    }
    pressContinue();
  }

  @And("I complete moving around points page for \"(12|10|8|4|0)\"")
  public void iCompleteMovingAroundPointsPageFor(String points) {
    if (points.equals("12")) {
      commonPage.findPageElementById(Ids.EleCheck.MOVING_POINTS).click();
    } else {
      commonPage
          .findPageElementById(Ids.EleCheck.MOVING_POINTS + ".MOVING_POINTS_" + points)
          .click();
    }
    pressContinue();
  }

  @And("I complete main reason page for \"(TERMILL|CHILDBULK|CHILDVEHIC|WALKD|ARMS|BLIND|NONE)\"")
  public void iCompleteMainReasonPageFor(String benefit) {
    if ("TERMILL".equals(benefit)) {
      commonPage.findPageElementById(MAIN_REASON_LIST).click();
    } else {
      commonPage.findPageElementById(MAIN_REASON_LIST + "." + benefit).click();
    }
    pressContinue();
  }

  @And("I complete may be eligible page")
  public void iCompleteMayBeEligible() {
    commonPage.findElementWithText("Start application").click();
  }

  @And("I complete what's your name page")
  public void iCompleteWhatsYourNamePage() {
    clearAndSendKeys(Ids.Person.NAME, "Test Username");
    commonPage.findPageElementById("hasBirthName").click();
    pressContinue();
  }

  @And("^I complete date of birth page for \"(CHILD|ADULT)\"")
  public void iCompleteDateOfBirthPage(String age_category) {
    Calendar now = Calendar.getInstance();
    int dob_year = 1900;

    if (age_category.equals("CHILD")) dob_year = now.get(Calendar.YEAR) - 10;
    else dob_year = now.get(Calendar.YEAR) - 30;

    clearAndSendKeys(DOB, "1");
    clearAndSendKeys(DOB_MONTH, "1");
    clearAndSendKeys(DOB_YEAR, Integer.toString(dob_year));
    pressContinue();
  }

  @And("^I complete eligible page$")
  public void iCompleteEligiblePage() {
    commonPage.findElementWithText("Start application").click();
  }

  @And("^I complete gender page for \"(Boy|Girl|Man|Woman|Identify in a different way)\"")
  public void iCompleteGenderPageFor(String gender) {
    if (gender.equals("Boy") || gender.equals("Man"))
      commonPage.findPageElementById(GENDER).click();
    else if (gender.equals("Girl") || gender.equals("Woman"))
      commonPage.findPageElementById(GENDER_FEMALE).click();
    else commonPage.findPageElementById(GENDER_UNSPECIFIED).click();

    pressContinue();
  }

  @And("^I complete describe health conditions page$")
  public void iCompleteDescribeHealthConditionsPage() throws Throwable {
    clearAndSendKeys("descriptionOfConditions", "Sample health condition");
    pressContinue();
  }

  @And("^I complete declaration page$")
  public void iCompleteDeclarationPage() {
    commonPage.findPageElementById("agreed").click();
    commonPage.findElementWithText("Submit application").click();
  }

  @And("^I complete planning points page for \"(12|10|8|4|0)\"")
  public void iCompletePlanningPointsPageFor(String points) {
    if ("12".equals(points)) {
      commonPage.findPageElementById(Ids.EleCheck.PLANNING_POINTS).click();
    } else {
      commonPage
          .findPageElementById(Ids.EleCheck.PLANNING_POINTS + ".PLANNING_POINTS_" + points)
          .click();
    }
    pressContinue();
  }

  @And("^I complete what makes walking difficult page for \"(HELP|PLAN|PAIN|DANGEROUS|NONE)\"$")
  public void iCompleteWhatMakesWalkingDifficultPageFor(String difficulty) {
    if (difficulty.equals("HELP")) {
      commonPage.findPageElementById(Ids.EleCheck.WALKING_DIFFICULTY_LIST).click();
    } else {
      commonPage
          .findPageElementById(Ids.EleCheck.WALKING_DIFFICULTY_LIST + "." + difficulty)
          .click();
    }
    pressContinue();
  }

  @And("^I complete dla allowance page for \"(YES|NO)\"$")
  public void iCompleteDlaAllowancePageFor(String option) {
    if ("YES".equals(option)) commonPage.findPageElementById(HAS_RECEIVED_DLA).click();
    else commonPage.findPageElementById(NEVER_RECEIVED_DLA).click();
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
    commonPage.findPageElementById(Ids.Person.NO_NI_LINK).click();
    commonPage.findPageElementById(Ids.Person.SKIP_WITHOUT_NI).click();
    pressContinue();
  }

  @And("^I complete the walking time page with option \"(CANTWALK|LESSMIN|FEWMIN|MORETEN)\"$")
  public void iCompleteTheWalkingTimePage(String option) throws Throwable {
    if ("CANTWALK".equals(option)) {
      commonPage.findPageElementById(Ids.Walkd.WALKING_TIME).click();
    } else {
      commonPage.findPageElementById(Ids.Walkd.WALKING_TIME + "." + option).click();
    }
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
    if ("YES".equals(option)) {
      commonPage.findPageElementById(Ids.EleCheck.RECEIVED_COMPENSATION).click();
    } else {
      commonPage
          .findPageElementById(Ids.EleCheck.RECEIVED_COMPENSATION + "." + option.toLowerCase())
          .click();
    }
    pressContinue();
  }

  @And("^I complete have permanent disability page for \"(YES|NO)\"$")
  public void iCompleteHavePermanentDisabilityDisabilityPageFor(String option) {
    if ("YES".equals(option)) {
      commonPage.findPageElementById(Ids.EleCheck.HAS_DISABILITY).click();
    } else {
      commonPage
          .findPageElementById(Ids.EleCheck.HAS_DISABILITY + "." + option.toLowerCase())
          .click();
    }
    pressContinue();
  }

  @And("^I complete has mental disorder page for \"(YES|NO)\"$")
  public void iCompleteHasMentalDisorderPageFor(String option) {
    if ("YES".equals(option)) {
      commonPage.findPageElementById(Ids.EleCheck.HAS_MENTAL_DISORDER).click();
    } else {
      commonPage
          .findPageElementById(Ids.EleCheck.HAS_MENTAL_DISORDER + "." + option.toLowerCase())
          .click();
    }
    pressContinue();
  }

  @And("^I complete has mobility component page for \"(YES|NO)\"$")
  public void iCompleteHasMobilityComponentPage(String option) {
    if ("YES".equals(option))
      commonPage.findPageElementById(Ids.EleCheck.AWARDED_HIGHER_RATE_MOBILITY).click();
    else
      commonPage
          .findPageElementById(Ids.EleCheck.AWARDED_HIGHER_RATE_MOBILITY + "." + "false")
          .click();
    pressContinue();
  }

  @And("^I complete the what makes walking difficult page$")
  public void iCompleteTheWhatMakesWalkingDifficultPage() throws Throwable {
    commonPage.findPageElementById(Ids.EleCheck.WHAT_WALKING_DIFFICULTY_LIST).click();
    pressContinue();
  }

  @And(
      "^I complete the what makes walking difficult page for \"(PAIN|BREATH|BALANCE|LONGTIME|DANGER|STRUGGLE|SOMELSE)\"$")
  public void iCompleteTheWhatMakesWalkingDifficultPageFor(String difficulty) throws Throwable {
    if ("PAIN".equals(difficulty)) {
      commonPage.findPageElementById(Ids.EleCheck.WHAT_WALKING_DIFFICULTY_LIST).click();
    } else {
      commonPage
          .findPageElementById(Ids.EleCheck.WHAT_WALKING_DIFFICULTY_LIST + difficulty)
          .click();
    }
    pressContinue();
  }

  private void clearAndSendKeys(String element, String value) {
    commonPage.findPageElementById(element).clear();
    commonPage.findPageElementById(element).sendKeys(value);
  }

  private void clickButtonById(String id) {
    commonPage.findPageElementById(id).click();
  }

  @And("^I complete the mobility aids page for \"(YES|NO)\"$")
  public void iCompleteTheMobilityAidsPage(String option) {
    if ("YES".equals(option)) {
      commonPage.findPageElementById(Ids.Walkd.MOBILITY_AID_OPTION).click();
      // Needs to update this to use id or data-uipath
      commonPage.findElementAddMobilityAid().click();
      commonPage.findPageElementById(MOBILITY_AID_TYPE_WHEELCHAIR).click();
      clearAndSendKeys(MOBILITY_AID_ADD_USAGE, "All the time");
      commonPage.findPageElementById(MOBILITY_AID_ADD_PROVIDED_CODE_PRESCRIBE).click();
      commonPage.findElementWithUiPath(MOBILITY_AID_ADD_CONFIRM_BUTTON).click();
    } else {
      commonPage.findPageElementById(Ids.Walkd.MOBILITY_AID_OPTION + option.toLowerCase()).click();
    }
    pressContinue();
  }

  @And("^I complete the treatments page for \"(YES|NO)\"$")
  public void iCompleteTheTreatmentsPage(String option) {

    if ("NO".equals(option)) {
      commonPage
          .findPageElementById(Ids.Walkd.TREATMENT_HAS_TREATMENT_OPTION + option.toLowerCase())
          .click();
    }

    if ("YES".equals(option)) {
      commonPage.findPageElementById(Ids.Walkd.TREATMENT_HAS_TREATMENT_OPTION).click();
      clickButtonById(Ids.Walkd.TREATMENT_ADD_FIRST_LINK);
      clearAndSendKeys(Ids.Walkd.TREATMENT_ADD_TREATMENT_DESCRIPTION, "Treatment description");
      clearAndSendKeys(Ids.Walkd.TREATMENT_ADD_TREATMENT_WHEN, "Treatment when");
      clickButtonById(Ids.Walkd.TREATMENT_ADD_CONFIRM_BUTTON);
      clickButtonById(Ids.Walkd.TREATMENT_REMOVE_LINK_PREFIX + "1");
      clickButtonById(Ids.Walkd.TREATMENT_ADD_FIRST_LINK);
      clearAndSendKeys(Ids.Walkd.TREATMENT_ADD_TREATMENT_DESCRIPTION, "Treatment description");
      clearAndSendKeys(Ids.Walkd.TREATMENT_ADD_TREATMENT_WHEN, "Treatment when");
      clickButtonById(Ids.Walkd.TREATMENT_ADD_CONFIRM_BUTTON);
    }
    pressContinue();
  }

  @And("^I complete the medications page for \"(YES|NO)\"$")
  public void iCompleteTheMedicationsPage(String option) {

    if ("YES".equals(option)) {
      commonPage.findPageElementById(Ids.Walkd.MEDICATION_HAS_MEDICATION_OPTION).click();
      addMedication(option);

      // find the first medication in the list and click
      commonPage.getHelper().findElement(By.xpath("//table[@id='medication-list']//a")).click();

      addMedication(option);
    } else {
      commonPage
          .findPageElementById(Ids.Walkd.MEDICATION_HAS_MEDICATION_OPTION + option.toLowerCase())
          .click();
    }
    pressContinue();
  }

  private void addMedication(String option) {
    clickButtonById(Ids.Walkd.MEDICATION_ADD_FIRST_LINK);
    clearAndSendKeys(Ids.Walkd.MEDICATION_ADD_MEDICATION_DESCRIPTION, "Paracetamol");
    if ("YES".equals(option)) {
      commonPage.findPageElementById(Ids.Walkd.MEDICATION_PRESCRIBED_OPTION).click();
    } else {
      commonPage
          .findPageElementById(Ids.Walkd.MEDICATION_PRESCRIBED_OPTION + "." + option.toLowerCase())
          .click();
    }
    clearAndSendKeys(Ids.Walkd.MEDICATION_DOSAGE_TEXT, "50mg");
    clearAndSendKeys(Ids.Walkd.MEDICATION_FREQUENCY_TEXT, "Every night");
    clickButtonById(Ids.Walkd.MEDICATION_ADD_CONFIRM_BUTTON);
  }

  @And("^I complete the already have a blue badge page for \"(YES|NO|YES BUT DON'T KNOW)\"$")
  public void iCompleteTheAlreadyHaveABlueBadgePageFor(String opt) throws Throwable {
    if ("YES BUT DON'T KNOW".equals(opt)) {
      commonPage.findPageElementById(AlreadyHaveBlueBadgePage.EXISTING_BADGE_OPTION).click();
      commonPage.findPageElementById(AlreadyHaveBlueBadgePage.BADGE_NUMBER_BYPASS_LINK).click();
    } else if ("YES".equals(opt)) {
      commonPage.findPageElementById(AlreadyHaveBlueBadgePage.EXISTING_BADGE_OPTION).click();
      commonPage.findPageElementById(AlreadyHaveBlueBadgePage.BADGE_NUMBER).sendKeys("AB 12 CD");
      pressContinue();
    } else {
      commonPage
          .findPageElementById(
              AlreadyHaveBlueBadgePage.EXISTING_BADGE_OPTION + "_" + opt.toLowerCase())
          .click();
      pressContinue();
    }
  }

  @And("^I complete does organisation care for \"(YES|NO)\"$")
  public void iCompleteOrganisationCaresPage(String option) {
    if ("YES".equals(option))
      commonPage.findPageElementById(Ids.EleCheck.ORGANISATION_CARES).click();
    else commonPage.findPageElementById(Ids.EleCheck.ORGANISATION_CARES + "." + "no").click();
    pressContinue();
  }

  @And("^I complete does organisation transport for \"(YES|NO)\"$")
  public void iCompleteOrganisationTransportsPage(String option) {
    if ("YES".equals(option))
      commonPage.findPageElementById(Ids.EleCheck.ORGANISATION_TRANSPORTS).click();
    else commonPage.findPageElementById(Ids.EleCheck.ORGANISATION_TRANSPORTS + "." + "no").click();
    pressContinue();
  }

  @And("^I complete the healthcare professionals page for \"(YES|NO)\"$")
  public void iCompleteTheHealthcareProfessionalsPage(String option) {
    if ("YES".equals(option)) {
      commonPage.findPageElementById(Ids.Eligibility.HEALTHCARE_PRO_HAS_OPTION).click();
    } else {
      commonPage
          .findPageElementById(Ids.Eligibility.HEALTHCARE_PRO_HAS_OPTION + option.toLowerCase())
          .click();
    }

    if ("YES".equals(option)) {
      clickButtonById(Ids.Eligibility.HEALTHCARE_PRO_ADD_FIRST_LINK);
      clearAndSendKeys(Ids.Eligibility.HEALTHCARE_PRO_ADD_DESCRIPTION, "Pro description");
      clearAndSendKeys(Ids.Eligibility.HEALTHCARE_PRO_ADD_LOCATION, "Pro Location");
      clickButtonById(Ids.Eligibility.HEALTHCARE_PRO_ADD_CONFIRM_BUTTON);
      clickButtonById(Ids.Eligibility.HEALTHCARE_PRO_REMOVE_LINK_PREFIX + "1");
      clickButtonById(Ids.Eligibility.HEALTHCARE_PRO_ADD_FIRST_LINK);
      clearAndSendKeys(Ids.Eligibility.HEALTHCARE_PRO_ADD_DESCRIPTION, "Pro description");
      clearAndSendKeys(Ids.Eligibility.HEALTHCARE_PRO_ADD_LOCATION, "Pro Location");
      clickButtonById(Ids.Eligibility.HEALTHCARE_PRO_ADD_CONFIRM_BUTTON);
    }
    pressContinue();
  }
}
