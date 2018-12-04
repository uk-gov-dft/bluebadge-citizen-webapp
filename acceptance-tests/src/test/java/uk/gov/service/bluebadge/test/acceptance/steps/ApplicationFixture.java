package uk.gov.service.bluebadge.test.acceptance.steps;

import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.EleCheck.*;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.Person.*;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.Person.GENDER_FEMALE;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.Person.GENDER_UNSPECIFIED;
import static uk.gov.service.bluebadge.test.acceptance.steps.Ids.Walkd.*;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import java.util.Calendar;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.service.bluebadge.test.acceptance.pages.site.*;
import uk.gov.service.bluebadge.test.acceptance.steps.site.ContactDetailsSteps;

public class ApplicationFixture extends AbstractSpringSteps {

  private CommonPage commonPage;
  private ContactDetailsSteps contactDetailsSteps;

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
      commonPage.selectRadioButton(ApplicantPage.APPLICANT_TYPE_OPTION_LIST);
    } else if (myselfOrOther.equalsIgnoreCase("someone else")) {
      commonPage.selectRadioButton(ApplicantPage.APPLICANT_TYPE_SOMELSE_OPTION);
    } else if (myselfOrOther.equalsIgnoreCase("organisation")) {
      commonPage.selectRadioButton(ApplicantPage.APPLICANT_TYPE_ORG_OPTION);
    }
    pressContinue();
  }

  @And("^I complete select council page$")
  public void iCompleteSelectCouncilPage() {
    iCompleteSelectCouncilPage("england");
  }

  @And("I complete select council page for \"(england|wales|scotland)\"")
  public void iCompleteSelectCouncilPage(String country) {
    iCompleteSelectCouncilPage(country, ChooseCouncilPage.COUNCIL_INPUT);
  }

  @And("I complete select council page for different service signpost")
  public void iCompleteSelectCouncilPageForDifferentServiceSignpost() {
    String council = "Runnymede";
    String fullCouncil = "Runnymede borough council";
    commonPage.findPageElementById(ChooseCouncilPage.COUNCIL_INPUT).sendKeys(council);
    commonPage.selectFromAutoCompleteList(ChooseCouncilPage.COUNCIL_INPUT, fullCouncil);
    pressContinue();
  }

  @And("^I complete registered council page for \"(england|wales|scotland)\"$")
  public void iCompleteRegisteredCouncilPage(String country){
    iCompleteSelectCouncilPage(country, ChooseCouncilPage.REGISTERED_COUNCIL_INPUT);
  }

  private void iCompleteSelectCouncilPage(String country, String inputId) {
    String council = "Worcester";
    String fullCouncil = "Worcester city council";
    if ("scotland".equalsIgnoreCase(country)) {
      council = "Aberdeenshire";
      fullCouncil = "Aberdeenshire council";
    } else if ("wales".equalsIgnoreCase(country)) {
      council = "Anglesey";
      fullCouncil = "Isle of Anglesey county council";
    }

    commonPage.findPageElementById(inputId).sendKeys(council);
    commonPage.selectFromAutoCompleteList(inputId, fullCouncil);
    pressContinue();
  }

  @And("I complete your local authority page")
  public void iCompleteYourAuthorityPage() {
    pressContinue();
  }

  @And("I complete receive benefit page for \"(PIP|DLA|AFRFCS|WPMS|NONE)\"")
  public void iCompleteReceiveBenefitPageFor(String benefit) {
    if (benefit.equals("PIP")) {
      commonPage.selectRadioButton(BenifitsPage.BENEFIT_RECEIVED_LIST);
    } else {
      commonPage.selectRadioButton(BenifitsPage.BENEFIT_RECEIVED_LIST + "." + benefit);
    }
    pressContinue();
  }

  @And("I complete moving around points page for \"(12|10|8|4|0)\"")
  public void iCompleteMovingAroundPointsPageFor(String points) {
    if (points.equals("12")) {
      commonPage.selectRadioButton(Ids.EleCheck.MOVING_POINTS);
    } else {
      commonPage.selectRadioButton(Ids.EleCheck.MOVING_POINTS + ".MOVING_POINTS_" + points);
    }
    pressContinue();
  }

  @And("I complete main reason page for \"(TERMILL|CHILDBULK|CHILDVEHIC|WALKD|ARMS|BLIND|NONE)\"")
  public void iCompleteMainReasonPageFor(String benefit) {
    if ("BLIND".equals(benefit)) {
      commonPage.selectRadioButton(MAIN_REASON_LIST);
    } else {
      commonPage.selectRadioButton(MAIN_REASON_LIST + "." + benefit);
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
    commonPage.selectRadioButton("hasBirthName");
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
    if (gender.equals("Boy") || gender.equals("Man")) commonPage.selectRadioButton(GENDER);
    else if (gender.equals("Girl") || gender.equals("Woman"))
      commonPage.selectRadioButton(GENDER_FEMALE);
    else commonPage.selectRadioButton(GENDER_UNSPECIFIED);

    pressContinue();
  }

  @And("^I complete describe health conditions page$")
  public void iCompleteDescribeHealthConditionsPage() {
    clearAndSendKeys("descriptionOfConditions", "Sample health condition");
    pressContinue();
  }

  @And("^I complete prove ID page with no documents")
  public void iCompleteProveIDPageWithNoDocuments() {
    commonPage.findPageElementById("cant-upload-text").click();
    commonPage.findPageElementById("continue-without-uploading").click();
  }

  @And("^I complete declaration page$")
  public void iCompleteDeclarationPage() {
    commonPage.selectRadioButton("agreed");
    commonPage.findElementWithText("Submit application").click();
  }

  @And("^I complete planning points page for \"(12|10|8|4|0)\"")
  public void iCompletePlanningPointsPageFor(String points) {
    if ("12".equals(points)) {
      commonPage.selectRadioButton(Ids.EleCheck.PLANNING_POINTS);
    } else {
      commonPage.selectRadioButton(Ids.EleCheck.PLANNING_POINTS + ".PLANNING_POINTS_" + points);
    }
    pressContinue();
  }

  @And("^I complete dla allowance page for \"(YES|NO)\"$")
  public void iCompleteDlaAllowancePageFor(String option) {
    if ("YES".equals(option)) commonPage.selectRadioButton(HAS_RECEIVED_DLA);
    else commonPage.selectRadioButton(NEVER_RECEIVED_DLA);
    pressContinue();
  }

  @And("^I complete address page$")
  public void iCompleteAddressPage() {
    clearAndSendKeys("buildingAndStreet", "120");
    clearAndSendKeys("optionalAddress", "London Road");
    clearAndSendKeys("townOrCity", "Manchester");
    clearAndSendKeys("postcode", "M4 1FS");

    pressContinue();
  }

  @And("^I complete NI number page$")
  public void iCompleteNINumberPage() {
    clearAndSendKeys(Ids.Person.NI, "ab 12 34 56 A");
    pressContinue();
  }

  @And("^I complete NI number page without a NI$")
  public void iCompleteNINumberPageWithoutNI() {
    commonPage.findPageElementById(Ids.Person.NO_NI_LINK).click();
    commonPage.findPageElementById(Ids.Person.SKIP_WITHOUT_NI).click();
    pressContinue();
  }

  @And("^I complete the walking time page with option \"(CANTWALK|LESSMIN|FEWMIN|MORETEN)\"$")
  public void iCompleteTheWalkingTimePage(String option) {
    if ("CANTWALK".equals(option)) {
      commonPage.selectRadioButton(Ids.Walkd.WALKING_TIME);
    } else {
      commonPage.selectRadioButton(Ids.Walkd.WALKING_TIME + "." + option);
    }
    pressContinue();
  }

  @And("^I complete where can you walk page$")
  public void iCompleteWhereCanYouWalkPage() {
    clearAndSendKeys(PLACE_CAN_WALK, "to the Post office on the High Street");
    clearAndSendKeys(TIME_TO_DESTINATION, "10 minutes");
    pressContinue();
  }

  @And("^I complete lump sum of the AFRFCS Scheme page for \"(YES|NO)\"$")
  public void iCompleteLumpSumToOfTheAFRFCSSchemePageFor(String option) {
    if ("YES".equals(option)) {
      commonPage.selectRadioButton(Ids.EleCheck.RECEIVED_COMPENSATION);
    } else {
      commonPage.selectRadioButton(Ids.EleCheck.RECEIVED_COMPENSATION + "." + option.toLowerCase());
    }
    pressContinue();
  }

  @And("^I complete have permanent disability page for \"(YES|NO)\"$")
  public void iCompleteHavePermanentDisabilityDisabilityPageFor(String option) {
    if ("YES".equals(option)) {
      commonPage.selectRadioButton(Ids.EleCheck.HAS_DISABILITY);
    } else {
      commonPage.selectRadioButton(Ids.EleCheck.HAS_DISABILITY + "." + option.toLowerCase());
    }
    pressContinue();
  }

  @And("^I complete has mental disorder page for \"(YES|NO)\"$")
  public void iCompleteHasMentalDisorderPageFor(String option) {
    if ("YES".equals(option)) {
      commonPage.selectRadioButton(Ids.EleCheck.HAS_MENTAL_DISORDER);
    } else {
      commonPage.selectRadioButton(Ids.EleCheck.HAS_MENTAL_DISORDER + "." + option.toLowerCase());
    }
    pressContinue();
  }

  @And("^I complete has mobility component page for \"(YES|NO)\"$")
  public void iCompleteHasMobilityComponentPage(String option) {
    if ("YES".equals(option))
      commonPage.selectRadioButton(Ids.EleCheck.AWARDED_HIGHER_RATE_MOBILITY);
    else commonPage.selectRadioButton(Ids.EleCheck.AWARDED_HIGHER_RATE_MOBILITY + "." + "false");
    pressContinue();
  }

  @And("^I complete medical equipment page$")
  public void iCompleteMedicalEquipmentPage() {
    commonPage.selectRadioButton(Ids.EleCheck.MEDICAL_EQUIPMENT);
    pressContinue();
  }

  @And(
      "^I complete medical equipment page for \"(PUMP|VENT|SUCTION|PARENT|SYRINGE|OXYADMIN|OXYSAT|CAST|OTHER)\"$")
  public void iCompleteMedicalEquipmentPage(String difficulty)  {
    if ("VENT".equals(difficulty)) {
      commonPage.selectRadioButton(Ids.EleCheck.MEDICAL_EQUIPMENT);
    } else {
      commonPage.selectRadioButton(Ids.EleCheck.MEDICAL_EQUIPMENT + difficulty);
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



  @And("^I complete the treatments page for \"(YES|NO)\"$")
  public void iCompleteTheTreatmentsPage(String option) {

    if ("NO".equals(option)) {
      commonPage.selectRadioButton(Ids.Walkd.TREATMENT_HAS_TREATMENT_OPTION + option.toLowerCase());
    }

    if ("YES".equals(option)) {
      commonPage.selectRadioButton(Ids.Walkd.TREATMENT_HAS_TREATMENT_OPTION);
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
      commonPage.selectRadioButton(Ids.Walkd.MEDICATION_HAS_MEDICATION_OPTION);
      addMedication(option);

      // find the first medication in the list and click
      commonPage.getHelper().findElement(By.xpath("//table[@id='medication-list']//a")).click();

      addMedication(option);
    } else {
      commonPage.selectRadioButton(
          Ids.Walkd.MEDICATION_HAS_MEDICATION_OPTION + option.toLowerCase());
    }
    pressContinue();
  }

  private void addMedication(String option) {
    clickButtonById(Ids.Walkd.MEDICATION_ADD_FIRST_LINK);
    clearAndSendKeys(Ids.Walkd.MEDICATION_ADD_MEDICATION_DESCRIPTION, "Paracetamol");
    if ("YES".equals(option)) {
      commonPage.selectRadioButton(Ids.Walkd.MEDICATION_PRESCRIBED_OPTION);
    } else {
      commonPage.selectRadioButton(
          Ids.Walkd.MEDICATION_PRESCRIBED_OPTION + "." + option.toLowerCase());
    }
    clearAndSendKeys(Ids.Walkd.MEDICATION_DOSAGE_TEXT, "50mg");
    clearAndSendKeys(Ids.Walkd.MEDICATION_FREQUENCY_TEXT, "Every night");
    clickButtonById(Ids.Walkd.MEDICATION_ADD_CONFIRM_BUTTON);
  }

  @And("^I complete the already have a blue badge page for \"(YES|NO|YES BUT DON'T KNOW)\"$")
  public void iCompleteTheAlreadyHaveABlueBadgePageFor(String opt) {
    if ("YES BUT DON'T KNOW".equals(opt)) {
      commonPage.selectRadioButton(AlreadyHaveBlueBadgePage.EXISTING_BADGE_OPTION);
      commonPage.findPageElementById(AlreadyHaveBlueBadgePage.BADGE_NUMBER_BYPASS_LINK).click();
    } else if ("YES".equals(opt)) {
      commonPage.selectRadioButton(AlreadyHaveBlueBadgePage.EXISTING_BADGE_OPTION);
      commonPage.findPageElementById(AlreadyHaveBlueBadgePage.BADGE_NUMBER).sendKeys("AB 12 CD");
      pressContinue();
    } else {
      commonPage.selectRadioButton(
          AlreadyHaveBlueBadgePage.EXISTING_BADGE_OPTION + "_" + opt.toLowerCase());
      pressContinue();
    }
  }

  @And("^I complete does organisation care for \"(YES|NO)\"$")
  public void iCompleteOrganisationCaresPage(String option) {
    if ("YES".equals(option)) commonPage.selectRadioButton(Ids.EleCheck.ORGANISATION_CARES);
    else commonPage.selectRadioButton(Ids.EleCheck.ORGANISATION_CARES + "." + "no");
    pressContinue();
  }

  @And("^I complete does organisation transport for \"(YES|NO)\"$")
  public void iCompleteOrganisationTransportsPage(String option) {
    if ("YES".equals(option)) commonPage.selectRadioButton(Ids.EleCheck.ORGANISATION_TRANSPORTS);
    else commonPage.selectRadioButton(Ids.EleCheck.ORGANISATION_TRANSPORTS + "." + "no");
    pressContinue();
  }

  @And("^I complete the healthcare professionals page for \"(YES|NO)\"$")
  public void iCompleteTheHealthcareProfessionalsPage(String option) {
    if ("YES".equals(option)) {
      commonPage.selectRadioButton(Ids.Eligibility.HEALTHCARE_PRO_HAS_OPTION);
    } else {
      commonPage.selectRadioButton(
          Ids.Eligibility.HEALTHCARE_PRO_HAS_OPTION + option.toLowerCase());
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

  @And("^I complete the how often do you drive page$")
  public void iCompleteHowOftenDoYouDrive() {
    clearAndSendKeys(Ids.Arms.HOW_OFTEN_DRIVE, "Once a week");
    pressContinue();
  }

  @And("^I complete the adapted vehicle page for \"(YES|NO)\"$")
  public void iCompleteAdaptedVehicle(String option) {
    commonPage
        .findPageElementById(Ids.Arms.IS_ADAPTED_VEHICLE_OPTION + "_" + option.toLowerCase())
        .click();
    if ("YES".equals(option)) {
      clearAndSendKeys(Ids.Arms.ADAPTED_VEHICLE_DESCRIPTIOM, "Vehicle description");
    }
    pressContinue();
  }

  @And("^I complete the difficulty with parking meters page$")
  public void iCompleteDifficultyWithParkingMeters() {
    clearAndSendKeys(Ids.Arms.DIFFICULTY_PARKING_METERS_DESC, "Parking meter difficulty");
    pressContinue();
  }
}
