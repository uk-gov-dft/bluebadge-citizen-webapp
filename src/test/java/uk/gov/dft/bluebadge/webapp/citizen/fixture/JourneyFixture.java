package uk.gov.dft.bluebadge.webapp.citizen.fixture;

import com.google.common.collect.Lists;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.GenderCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.HowProvidedCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingDifficultyTypeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingLengthOfTimeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.LocalAuthorityRefData;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.Nation;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.component.CompoundDate;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantNameForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantType;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ContactDetailsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.DateOfBirthForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.EligibleForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.EnterAddressForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ExistingBadgeForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.GenderForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthConditionsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthcareProfessionalAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthcareProfessionalListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HigherRateMobilityForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MayBeEligibleForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MobilityAidAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MobilityAidListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.NinoForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ReceiveBenefitsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.TreatmentAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.TreatmentListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.WhereCanYouWalkForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.YourIssuingAuthorityForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.afcs.CompensationSchemeForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.afcs.MentalDisorderForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.MainReasonForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.WalkingDifficultyForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.pip.PipDlaQuestionForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.pip.PipMovingAroundForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.pip.PipPlanningJourneyForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.MedicationAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.MedicationListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.WalkingTimeForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.WhatMakesWalkingDifficultForm;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.AFRFCS;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.ARMS;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.BLIND;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.CHILDBULK;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.CHILDVEHIC;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.DLA;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.NONE;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.PIP;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.TERMILL;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.WALKD;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.WPMS;
import static uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.Nation.SCO;

public class JourneyFixture {

  public interface Values {
    String TREATMENT_DESCRIPTION = "Desc";
    String TREATMENT_WHEN = "When";
    String NO = "no";
    String YES = "yes";
    String MEDICATION_NAME = "name";
    String MEDICATION_FREQUENCY = "Frequency";
    String MEDICATION_DOSAGE = "Dosage";
    String MOBILITY_USAGE = "Usage";
    HowProvidedCodeField MOBILITY_HOW_PROVIDED = HowProvidedCodeField.PRESCRIBE;
    MobilityAidAddForm.AidType MOBILITY_AID_TYPE = MobilityAidAddForm.AidType.SCOOTER;
    WalkingLengthOfTimeCodeField WALKING_TIME = WalkingLengthOfTimeCodeField.LESSMIN;
    List<WalkingDifficultyTypeCodeField> WHAT_MAKES_WALKING_DIFFICULT =
        Lists.newArrayList(
            WalkingDifficultyTypeCodeField.PAIN, WalkingDifficultyTypeCodeField.SOMELSE);
    String BIRTH_NAME = "Birth";
    String FULL_NAME = "Full";
    GenderCodeField GENDER = GenderCodeField.FEMALE;
    String NINO = "NS123456D";
    String LA_SHORT_CODE = "ABERD";
    String DESCRIPTION_OF_CONDITIONS = "test description";
    String APPLICANT_TYPE = ApplicantType.SOMEONE_ELSE.toString();
    String ADDRESS_LINE_1 = "High Street 1";
    String ADDRESS_LINE_2 = "District";
    String TOWN_OR_CITY = "London";
    String CONTACT_NAME = "Contact Name";
    String POSTCODE = "BR4 9NA";
    String PRIMARY_PHONE_NO = "01270646362";
    String WHERE_WALK_DESTINATION = "London";
    String WHERE_WALK_TIME = "10 minutes";
    String WHAT_WALKING_SOME_ELSE_DESC = "Some description of walking";
    String DOB_AS_EQUAL_TO_STRING = "1990-01-01";
    String SECONDARY_PHONE_NO = "07970123456";
    String EMAIL_ADDRESS = "a@b.c";
    String HEALTHCARE_PRO_LOCATION = "location";
    String HEALTHCARE_PRO_NAME = "name";
    String EXISTING_BADGE_NO = "oldun";
  }

  public static ExistingBadgeForm getExistingBadgeForm() {
    return ExistingBadgeForm.builder()
        .hasExistingBadge(true)
        .badgeNumber(Values.EXISTING_BADGE_NO)
        .build();
  }

  private static HealthcareProfessionalListForm getHealthcareProfessionalListForm() {
    HealthcareProfessionalAddForm addForm = new HealthcareProfessionalAddForm();
    addForm.setHealthcareProfessionalName(Values.HEALTHCARE_PRO_NAME);
    addForm.setHealthcareProfessionalLocation(Values.HEALTHCARE_PRO_LOCATION);
    return HealthcareProfessionalListForm.builder()
        .hasHealthcareProfessional(Values.YES)
        .healthcareProfessionals(Lists.newArrayList(addForm))
        .build();
  }

  public static TreatmentListForm getTreatmentListForm() {
    TreatmentAddForm treatmentAddForm = new TreatmentAddForm();
    treatmentAddForm.setTreatmentDescription(Values.TREATMENT_DESCRIPTION);
    treatmentAddForm.setTreatmentWhen(Values.TREATMENT_WHEN);

    return TreatmentListForm.builder()
        .hasTreatment(Values.YES)
        .treatments(Lists.newArrayList(treatmentAddForm))
        .build();
  }

  public static MedicationListForm getMedicationListForm() {
    MedicationAddForm medicationAddForm = new MedicationAddForm();
    medicationAddForm.setPrescribed(Values.YES);
    medicationAddForm.setName(Values.MEDICATION_NAME);
    medicationAddForm.setFrequency(Values.MEDICATION_FREQUENCY);
    medicationAddForm.setDosage(Values.MEDICATION_DOSAGE);

    return MedicationListForm.builder()
        .hasMedication(Values.YES)
        .medications(Lists.newArrayList(medicationAddForm))
        .build();
  }

  public static MobilityAidListForm getMobilityAidListForm() {
    MobilityAidAddForm mobilityAidAddForm = new MobilityAidAddForm();
    mobilityAidAddForm.setHowProvidedCodeField(Values.MOBILITY_HOW_PROVIDED);
    mobilityAidAddForm.setAidType(Values.MOBILITY_AID_TYPE);
    mobilityAidAddForm.setUsage(Values.MOBILITY_USAGE);

    return MobilityAidListForm.builder()
        .hasWalkingAid(Values.YES)
        .mobilityAids(Lists.newArrayList(mobilityAidAddForm))
        .build();
  }

  public static WalkingTimeForm getWalkingTimeForm() {
    return WalkingTimeForm.builder().walkingTime(Values.WALKING_TIME).build();
  }

  public static WhatMakesWalkingDifficultForm getWhatMakesWalkingDifficultForm() {
    return WhatMakesWalkingDifficultForm.builder()
        .whatWalkingDifficulties(Values.WHAT_MAKES_WALKING_DIFFICULT)
        .somethingElseDescription(Values.WHAT_WALKING_SOME_ELSE_DESC)
        .build();
  }

  private static WhereCanYouWalkForm getWhereCanYouWalkForm() {
    return WhereCanYouWalkForm.builder()
        .destinationToHome(Values.WHERE_WALK_DESTINATION)
        .timeToDestination(Values.WHERE_WALK_TIME)
        .build();
  }

  private static ApplicantNameForm getApplicantNameForm() {
    return ApplicantNameForm.builder()
        .fullName(Values.FULL_NAME)
        .hasBirthName(true)
        .birthName(Values.BIRTH_NAME)
        .build();
  }

  private static GenderForm getGenderForm() {
    return GenderForm.builder().gender(Values.GENDER).build();
  }

  private static ApplicantForm getApplicantForm() {
    return ApplicantForm.builder().applicantType(Values.APPLICANT_TYPE).build();
  }

  private static YourIssuingAuthorityForm getYourIssuingAuthorityForm() {
    return YourIssuingAuthorityForm.builder().localAuthorityShortCode(Values.LA_SHORT_CODE).build();
  }

  private static DateOfBirthForm getDateOfBirthForm() {
    return DateOfBirthForm.builder().dateOfBirth(new CompoundDate("1", "1", "1990")).build();
  }

  private static HealthConditionsForm getHealthConditionsForm() {
    return HealthConditionsForm.builder()
        .descriptionOfConditions(Values.DESCRIPTION_OF_CONDITIONS)
        .build();
  }

  public static HigherRateMobilityForm getHightRateMobilityForm() {
    return HigherRateMobilityForm.builder().awardedHigherRateMobility(Boolean.TRUE).build();
  }

  private static EnterAddressForm getEnterAddressForm() {
    return EnterAddressForm.builder()
        .buildingAndStreet(Values.ADDRESS_LINE_1)
        .optionalAddress(Values.ADDRESS_LINE_2)
        .townOrCity(Values.TOWN_OR_CITY)
        .postcode(Values.POSTCODE)
        .build();
  }

  private static ContactDetailsForm getContactDetailsForm() {
    return ContactDetailsForm.builder()
        .primaryPhoneNumber(Values.PRIMARY_PHONE_NO)
        .emailAddress(Values.EMAIL_ADDRESS)
        .secondaryPhoneNumber(Values.SECONDARY_PHONE_NO)
        .fullName(Values.CONTACT_NAME)
        .build();
  }

  public static NinoForm getNinoForm() {
    return NinoForm.builder().nino(Values.NINO).build();
  }

  public static PipMovingAroundForm getPipMovingAroundForm() {
    return PipMovingAroundForm.builder()
        .movingAroundPoints(PipMovingAroundForm.PipMovingAroundOption.MOVING_POINTS_4)
        .build();
  }

  public static StepForm getPipPlanningJourneyForm() {
    return PipPlanningJourneyForm.builder()
        .planningJourneyOption(PipPlanningJourneyForm.PipPlanningJourneyOption.PLANNING_POINTS_0)
        .build();
  }

  public static LocalAuthorityRefData getLocalAuthorityRefData(Nation nation) {
    LocalAuthorityRefData localAuthorityRefData = new LocalAuthorityRefData();
    localAuthorityRefData.setShortCode(Values.LA_SHORT_CODE);
    LocalAuthorityRefData.LocalAuthorityMetaData meta =
        new LocalAuthorityRefData.LocalAuthorityMetaData();
    meta.setNation(nation);
    localAuthorityRefData.setLocalAuthorityMetaData(Optional.of(meta));
    return localAuthorityRefData;
  }

  public static CompensationSchemeForm getCompensationSchemeForm() {
    return CompensationSchemeForm.builder().hasReceivedCompensation(Boolean.TRUE).build();
  }

  public static MentalDisorderForm getMentalDisorderForm() {
    return MentalDisorderForm.builder().hasMentalDisorder(Boolean.TRUE).build();
  }

  public static WalkingDifficultyForm getWalkingDifficultyForm() {
    return WalkingDifficultyForm.builder()
        .walkingDifficulty(WalkingDifficultyForm.WalkingDifficulty.HELP)
        .build();
  }

  private static StepDefinition getPreviousStep(StepDefinition step) {
    // Special cases
    if (StepDefinition.TREATMENT_LIST == step || StepDefinition.TREATMENT_ADD == step) {
      return StepDefinition.WHERE_CAN_YOU_WALK;
    }
    if (StepDefinition.MEDICATION_ADD == step) {
      return StepDefinition.MEDICATION_LIST;
    }
    if (StepDefinition.HEALTHCARE_PROFESSIONALS_ADD == step) {
      return StepDefinition.HEALTHCARE_PROFESSIONAL_LIST;
    }
    if (StepDefinition.NAME == step) {
      return StepDefinition.ELIGIBLE;
    }
    for (StepDefinition previousStep : StepDefinition.values()) {
      if (previousStep.getNext().contains(step)) {
        return previousStep;
      }
    }
    return StepDefinition.APPLICANT_TYPE;
  }

  public static Journey getDefaultJourneyToPreviousStep(StepDefinition step) {
    return getDefaultJourneyToStep(getPreviousStep(step), ARMS, SCO);
  }

  public static Journey getDefaultJourneyToPreviousStep(
      StepDefinition step, EligibilityCodeField eligibilityType) {
    return getDefaultJourneyToStep(getPreviousStep(step), eligibilityType, SCO);
  }

  public static Journey getDefaultJourneyToStep(StepDefinition step) {
    return getDefaultJourneyToStep(step, EligibilityCodeField.PIP, SCO);
  }

  public static Journey getDefaultJourneyToStep(
      StepDefinition step, EligibilityCodeField eligibility) {
    return getDefaultJourneyToStep(step, eligibility, SCO);
  }

  public static Journey getDefaultJourneyToStep(
      StepDefinition step, EligibilityCodeField eligibility, Nation nation) {
    Journey journey = new Journey();

    // Preamble section
    journey.setApplicantForm(getApplicantForm());
    if (StepDefinition.APPLICANT_TYPE == step) return journey;
    journey.setYourIssuingAuthorityForm(getYourIssuingAuthorityForm());
    journey.setLocalAuthority(getLocalAuthorityRefData(nation));
    if (StepDefinition.YOUR_ISSUING_AUTHORITY == step) return journey;
    journey.setExistingBadgeForm(getExistingBadgeForm());
    if (StepDefinition.EXISTING_BADGE == step) return journey;

    // Check Eligibility section
    if (EligibilityCodeField.PIP == eligibility) {
      journey.setFormForStep(ReceiveBenefitsForm.builder().benefitType(PIP).build());
      if (StepDefinition.RECEIVE_BENEFITS == step) return journey;
      journey.setFormForStep(getPipMovingAroundForm());
      if (StepDefinition.PIP_MOVING_AROUND == step) return journey;
      journey.setFormForStep(getPipPlanningJourneyForm());
      if (StepDefinition.PIP_PLANNING_JOURNEY == step) return journey;
      journey.setFormForStep(
          getPipDlaForm());
      if (StepDefinition.PIP_DLA == step) return journey;
      journey.setFormForStep(new EligibleForm());
      if (StepDefinition.ELIGIBLE == step) return journey;
    }
    if (EligibilityCodeField.WALKD == eligibility) {
      journey.setFormForStep(ReceiveBenefitsForm.builder().benefitType(WALKD).build());
      if (StepDefinition.RECEIVE_BENEFITS == step) return journey;
      journey.setFormForStep(MainReasonForm.builder().mainReasonOption(WALKD).build());
      if (StepDefinition.MAIN_REASON == step) return journey;
      journey.setFormForStep(getWalkingDifficultyForm());
      if (StepDefinition.WALKING_DIFFICULTY == step) return journey;
      journey.setFormForStep(new MayBeEligibleForm());
    }
    if (EligibilityCodeField.CHILDBULK == eligibility) {
      journey.setFormForStep(ReceiveBenefitsForm.builder().benefitType(NONE).build());
      if (StepDefinition.RECEIVE_BENEFITS == step) return journey;
      journey.setFormForStep(MainReasonForm.builder().mainReasonOption(CHILDBULK).build());
      if (StepDefinition.MAIN_REASON == step) return journey;
    }
    if (EligibilityCodeField.BLIND == eligibility) {
      journey.setFormForStep(ReceiveBenefitsForm.builder().benefitType(NONE).build());
      if (StepDefinition.RECEIVE_BENEFITS == step) return journey;
      journey.setFormForStep(MainReasonForm.builder().mainReasonOption(BLIND).build());
      if (StepDefinition.MAIN_REASON == step) return journey;
    }
    if (EligibilityCodeField.NONE == eligibility) {
      journey.setFormForStep(ReceiveBenefitsForm.builder().benefitType(NONE).build());
      if (StepDefinition.RECEIVE_BENEFITS == step) return journey;
      journey.setFormForStep(MainReasonForm.builder().mainReasonOption(NONE).build());
      // End of journey - not eligible page
      return journey;
    }
    if (EligibilityCodeField.AFRFCS == eligibility) {
      journey.setFormForStep(ReceiveBenefitsForm.builder().benefitType(AFRFCS).build());
      if (StepDefinition.RECEIVE_BENEFITS == step) return journey;
      journey.setFormForStep(getCompensationSchemeForm());
      if (StepDefinition.AFCS_COMPENSATION_SCHEME == step) return journey;
      journey.setFormForStep(getMentalDisorderForm());
      if (StepDefinition.AFCS_MENTAL_DISORDER == step) return journey;
    }
    if (EligibilityCodeField.TERMILL == eligibility) {
      journey.setFormForStep(ReceiveBenefitsForm.builder().benefitType(NONE).build());
      if (StepDefinition.RECEIVE_BENEFITS == step) return journey;
      journey.setFormForStep(MainReasonForm.builder().mainReasonOption(TERMILL).build());
      // End of journey
      return journey;
    }
    if (EligibilityCodeField.DLA == eligibility || step == StepDefinition.HIGHER_RATE_MOBILITY) {
      journey.setFormForStep(ReceiveBenefitsForm.builder().benefitType(NONE).build());
      if (StepDefinition.RECEIVE_BENEFITS == step) return journey;
      journey.setFormForStep(MainReasonForm.builder().mainReasonOption(DLA).build());
      if (StepDefinition.MAIN_REASON == step) return journey;
      journey.setFormForStep(getHightRateMobilityForm());
      if (StepDefinition.HIGHER_RATE_MOBILITY == step) return journey;
    }

    journey.setHealthConditionsForm(getHealthConditionsForm());

    journey.setContactDetailsForm(getContactDetailsForm());

    // Start application Section
    journey.setApplicantNameForm(getApplicantNameForm());
    if (StepDefinition.NAME == step) return journey;
    journey.setDateOfBirthForm(getDateOfBirthForm());
    if (StepDefinition.DOB == step) return journey;
    journey.setGenderForm(getGenderForm());
    if (StepDefinition.GENDER == step) return journey;
    journey.setNinoForm(getNinoForm());
    if (StepDefinition.NINO == step) return journey;
    journey.setEnterAddressForm(getEnterAddressForm());
    if (StepDefinition.ADDRESS == step) return journey;

    // Eligibility specific section
    if (WALKD == eligibility) {
      journey.setWhatMakesWalkingDifficultForm(getWhatMakesWalkingDifficultForm());
      if (StepDefinition.WHAT_WALKING_DIFFICULTIES == step) return journey;
      journey.setMobilityAidListForm(getMobilityAidListForm());
      if (StepDefinition.MOBILITY_AID_LIST == step) return journey;
      journey.setWalkingTimeForm(getWalkingTimeForm());
      if (StepDefinition.WALKING_TIME == step) return journey;
      journey.setWhereCanYouWalkForm(getWhereCanYouWalkForm());
      if (StepDefinition.WHERE_CAN_YOU_WALK == step) return journey;
      journey.setFormForStep(getTreatmentListForm());
      if (StepDefinition.TREATMENT_LIST == step) return journey;
      journey.setFormForStep(getMedicationListForm());
      if (StepDefinition.MEDICATION_LIST == step) return journey;
    }

    journey.setHealthcareProfessionalListForm(getHealthcareProfessionalListForm());
    return journey;
  }

  public static PipDlaQuestionForm getPipDlaForm() {
    return PipDlaQuestionForm.builder()
        .receivedDlaOption(PipDlaQuestionForm.PipReceivedDlaOption.HAS_RECEIVED_DLA)
        .build();
  }

  public static Journey getDefaultJourney() {
    return getDefaultJourneyToStep(null);
  }

  public static void configureMockJourney(
      Journey mockJourney, EligibilityCodeField eligibilityType) {

    // Eligibility type
    when(mockJourney.getEligibilityCode()).thenReturn(eligibilityType);
    if (EnumSet.of(PIP, DLA, AFRFCS, WPMS).contains(eligibilityType)) {
      when(mockJourney.getFormForStep(StepDefinition.RECEIVE_BENEFITS))
          .thenReturn(ReceiveBenefitsForm.builder().benefitType(eligibilityType).build());
      when(mockJourney.getFormForStep(StepDefinition.MAIN_REASON)).thenReturn(null);
    } else {
      when(mockJourney.getFormForStep(StepDefinition.RECEIVE_BENEFITS))
          .thenReturn(ReceiveBenefitsForm.builder().benefitType(NONE).build());
      when(mockJourney.getFormForStep(StepDefinition.MAIN_REASON))
          .thenReturn(MainReasonForm.builder().mainReasonOption(eligibilityType).build());
    }

    // Common steps
    when(mockJourney.getLocalAuthority()).thenReturn(getLocalAuthorityRefData(SCO));
    when(mockJourney.getFormForStep(StepDefinition.NAME)).thenReturn(getApplicantNameForm());
    when(mockJourney.getFormForStep(StepDefinition.GENDER)).thenReturn(getGenderForm());
    when(mockJourney.getFormForStep(StepDefinition.NINO)).thenReturn(getNinoForm());
    when(mockJourney.getFormForStep(StepDefinition.DOB)).thenReturn(getDateOfBirthForm());
    when(mockJourney.getFormForStep(StepDefinition.CONTACT_DETAILS))
        .thenReturn(getContactDetailsForm());
    when(mockJourney.getFormForStep(StepDefinition.ADDRESS)).thenReturn(getEnterAddressForm());

    if (WALKD == eligibilityType) {
      when(mockJourney.getFormForStep(StepDefinition.TREATMENT_LIST))
          .thenReturn(getTreatmentListForm());
      when(mockJourney.getFormForStep(StepDefinition.MEDICATION_LIST))
          .thenReturn(getMedicationListForm());
      when(mockJourney.getFormForStep(StepDefinition.MOBILITY_AID_LIST))
          .thenReturn(getMobilityAidListForm());
      when(mockJourney.getFormForStep(StepDefinition.WALKING_TIME))
          .thenReturn(getWalkingTimeForm());
      when(mockJourney.getFormForStep(StepDefinition.WHAT_WALKING_DIFFICULTIES))
          .thenReturn(getWhatMakesWalkingDifficultForm());
    } else {
      when(mockJourney.getFormForStep(StepDefinition.TREATMENT_LIST)).thenReturn(null);
      when(mockJourney.getFormForStep(StepDefinition.MEDICATION_LIST)).thenReturn(null);
      when(mockJourney.getFormForStep(StepDefinition.MOBILITY_AID_LIST)).thenReturn(null);
      when(mockJourney.getFormForStep(StepDefinition.WALKING_TIME)).thenReturn(null);
      when(mockJourney.getFormForStep(StepDefinition.WHAT_WALKING_DIFFICULTIES)).thenReturn(null);
    }
    if (EnumSet.of(WALKD, CHILDVEHIC, CHILDBULK).contains(eligibilityType)) {
      when(mockJourney.getFormForStep(StepDefinition.HEALTHCARE_PROFESSIONAL_LIST))
          .thenReturn(getHealthcareProfessionalListForm());
    } else {
      when(mockJourney.getFormForStep(StepDefinition.HEALTHCARE_PROFESSIONAL_LIST))
          .thenReturn(null);
    }

    when(mockJourney.getFormForStep(StepDefinition.HEALTH_CONDITIONS))
        .thenReturn(getHealthConditionsForm());
    when(mockJourney.getDescriptionOfCondition()).thenReturn(Values.DESCRIPTION_OF_CONDITIONS);
    when(mockJourney.getFormForStep(StepDefinition.EXISTING_BADGE))
        .thenReturn(getExistingBadgeForm());
  }

  public static void configureMockJourney(Journey mockJourney) {
    configureMockJourney(mockJourney, PIP);
  }

  public static class JourneyBuilder {
    Journey journey;

    public JourneyBuilder() {
      journey = getDefaultJourney();
    }

    public JourneyBuilder setEnglishLocalAuthority() {
      return setNation(Nation.ENG);
    }

    public JourneyBuilder setScottishLocalAuthority() {
      return setNation(SCO);
    }

    public JourneyBuilder setWelshLocalAuthority() {
      return setNation(Nation.WLS);
    }

    public JourneyBuilder setDateOfBirth(String year, String month, String day) {
      CompoundDate date = new CompoundDate();
      date.setDay(day);
      date.setMonth(month);
      date.setYear(year);
      journey.getDateOfBirthForm().setDateOfBirth(date);
      return this;
    }

    private JourneyBuilder setNation(Nation nation) {
      LocalAuthorityRefData.LocalAuthorityMetaData meta =
          new LocalAuthorityRefData.LocalAuthorityMetaData();
      meta.setNation(nation);
      LocalAuthorityRefData localAuthorityRefData = new LocalAuthorityRefData();
      localAuthorityRefData.setLocalAuthorityMetaData(Optional.of(meta));
      journey.setLocalAuthority(localAuthorityRefData);
      return this;
    }

    public Journey build() {
      return journey;
    }
  }
}
