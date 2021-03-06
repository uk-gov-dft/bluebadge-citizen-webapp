package uk.gov.dft.bluebadge.webapp.citizen.fixture;

import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.BulkyMedicalEquipmentTypeCodeField.OTHER;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.BulkyMedicalEquipmentTypeCodeField.PUMP;
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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.BreathlessnessCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.GenderCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.HowProvidedCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingDifficultyTypeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingLengthOfTimeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.payment.model.PaymentStatusResponse;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.LocalAuthorityRefData;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.Nation;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.JourneyArtifact;
import uk.gov.dft.bluebadge.webapp.citizen.model.component.CompoundDate;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantNameForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantType;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.BadgePaymentForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.BadgePaymentReturnForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ChooseYourCouncilForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ContactDetailsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.DateOfBirthForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.DeclarationSubmitForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.EligibleForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.EnterAddressForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ExistingBadgeForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.FindYourCouncilForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.GenderForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthConditionsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthcareProfessionalAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthcareProfessionalListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HigherRateMobilityForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MayBeEligibleForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MedicalEquipmentForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MobilityAidAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MobilityAidListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.NinoForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.NotPaidForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.OrganisationMayBeEligibleForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.PaymentUnavailableForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ProveAddressForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ProveBenefitForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ProveIdentityForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ProvidePhotoForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ReceiveBenefitsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.TreatmentAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.TreatmentListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.UploadBenefitForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.UploadSupportingDocumentsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.WhereCanYouWalkForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.YourIssuingAuthorityForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.afcs.CompensationSchemeForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.afcs.DisabilityForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.afcs.MentalDisorderForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.arms.ArmsAdaptedVehicleForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.arms.ArmsDifficultyParkingMetersForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.arms.ArmsHowOftenDriveForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.blind.PermissionForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.blind.RegisteredCouncilForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.blind.RegisteredForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.MainReasonForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.WalkingDifficultyForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.organisation.OrganisationCareForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.organisation.OrganisationTransportForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.pip.PipDlaQuestionForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.pip.PipMovingAroundForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.pip.PipPlanningJourneyForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.BreathlessnessForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.MedicationAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.MedicationListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.WalkingTimeForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.WhatMakesWalkingDifficultForm;

public class JourneyFixture {

  public static class Values {
    static final String TREATMENT_DESCRIPTION = "Desc";
    static final String TREATMENT_WHEN = "When";
    static final String NO = "no";
    static final String YES = "yes";
    static final String MEDICATION_NAME = "name";
    static final String MEDICATION_FREQUENCY = "Frequency";
    static final String MEDICATION_DOSAGE = "Dosage";
    static final String MOBILITY_USAGE = "Usage";
    static final HowProvidedCodeField MOBILITY_HOW_PROVIDED = HowProvidedCodeField.PRESCRIBE;
    static final String MOBILITY_AID_TYPE = "Scooter";
    static final WalkingLengthOfTimeCodeField WALKING_TIME = WalkingLengthOfTimeCodeField.LESSMIN;
    static final List<WalkingDifficultyTypeCodeField> WHAT_MAKES_WALKING_DIFFICULT =
        Lists.newArrayList(
            WalkingDifficultyTypeCodeField.PAIN,
            WalkingDifficultyTypeCodeField.BREATH,
            WalkingDifficultyTypeCodeField.SOMELSE);
    static final List<WalkingDifficultyTypeCodeField>
        WHAT_MAKES_WALKING_DIFFICULT_WITHOUT_SOMETHING_ELSE =
            Lists.newArrayList(WalkingDifficultyTypeCodeField.PAIN);
    static final List<BreathlessnessCodeField> BREATHLESSNESS_TYPES =
        Lists.newArrayList(BreathlessnessCodeField.UPHILL, BreathlessnessCodeField.OTHER);
    static final List<BreathlessnessCodeField> BREATHLESSNESS_TYPE =
        Lists.newArrayList(BreathlessnessCodeField.UPHILL);
    public static final String BIRTH_NAME = "Birth";
    public static final String FULL_NAME = "Full";
    public static final GenderCodeField GENDER = GenderCodeField.FEMALE;
    public static final String NINO = "NS123456D";
    public static final String LA_SHORT_CODE = "ABERD";
    static final String DESCRIPTION_OF_CONDITIONS = "test description";
    static final ApplicantType APPLICANT_TYPE = ApplicantType.SOMEONE_ELSE;
    static final String APPLICANT_TYPE_STRING = APPLICANT_TYPE.name();
    public static final String ADDRESS_LINE_1 = "High Street 1";
    public static final String ADDRESS_LINE_2 = "District";
    public static final String TOWN_OR_CITY = "London";
    public static final String CONTACT_NAME = "Contact Name";
    public static final String POSTCODE = "BR4 9NA";
    public static final String PRIMARY_PHONE_NO = " 0 1 27 064 6362";
    public static final String PRIMARY_PHONE_NO_TRIMMED = "01270646362";
    static final String WHERE_WALK_DESTINATION = "London";
    static final String WHERE_WALK_TIME = "10 minutes";
    static final String WHAT_WALKING_SOME_ELSE_DESC = "Some description of walking";
    static final String BREATHLESSNESS_OTHER_DESC = "Some description of breathlessness";
    public static final String DOB_AS_EQUAL_TO_STRING = "1990-01-01";
    public static final String SECONDARY_PHONE_NO = "+4479 701 23456 ";
    public static final String SECONDARY_PHONE_NO_TRIMMED = "+447970123456";
    public static final String EMAIL_ADDRESS = "a@b.c";
    static final String HEALTHCARE_PRO_LOCATION = "location";
    static final String HEALTHCARE_PRO_NAME = "name";
    public static final String EXISTING_BADGE_NO = "1234AB";
    static final CompoundDate DOB_ADULT = new CompoundDate("1", "1", "1990");
    public static final CompoundDate DOB_CHILD =
        new CompoundDate("1", "1", String.valueOf(LocalDate.now().getYear()));
    public static final String ARMS_ADAPTED_VEH_DESC = "Adapted vehicle desc";
    public static final String ARMS_HOW_OFTEN_DRIVE = "How often drive";
    public static final String ARMS_PARKING_METERS = "Parking metrs";
    public static final Boolean IS_ADAPTED_VEHICLE = Boolean.TRUE;
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

  public static WhatMakesWalkingDifficultForm
      getWhatMakesWalkingDifficultFormWithoutSomethingElse() {
    return WhatMakesWalkingDifficultForm.builder()
        .whatWalkingDifficulties(Values.WHAT_MAKES_WALKING_DIFFICULT_WITHOUT_SOMETHING_ELSE)
        .build();
  }

  public static BreathlessnessForm getBreathlessnessForm() {
    return BreathlessnessForm.builder()
        .breathlessnessTypes(Values.BREATHLESSNESS_TYPES)
        .breathlessnessOtherDescription(Values.BREATHLESSNESS_OTHER_DESC)
        .build();
  }

  public static BreathlessnessForm getBreathlessnessFormWithoutOther() {
    return BreathlessnessForm.builder().breathlessnessTypes(Values.BREATHLESSNESS_TYPE).build();
  }

  private static WhereCanYouWalkForm getWhereCanYouWalkForm() {
    return WhereCanYouWalkForm.builder()
        .destinationToHome(Values.WHERE_WALK_DESTINATION)
        .timeToDestination(Values.WHERE_WALK_TIME)
        .build();
  }

  private static UploadSupportingDocumentsForm getUploadSupportingDocumentsForm() {
    return UploadSupportingDocumentsForm.builder()
        .journeyArtifacts(Lists.newArrayList(buildJourneyArtifact("http://s3/supportingDocument")))
        .build();
  }

  private static ProveAddressForm getProveAddressForm() {
    return ProveAddressForm.builder()
        .journeyArtifact(buildJourneyArtifact("http://s3/proveAddress"))
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

  private static ApplicantForm getApplicantForm(ApplicantType type) {
    return ApplicantForm.builder().applicantType(type.name()).build();
  }

  public static YourIssuingAuthorityForm getYourIssuingAuthorityForm() {
    return YourIssuingAuthorityForm.builder().localAuthorityShortCode(Values.LA_SHORT_CODE).build();
  }

  private static DateOfBirthForm getDateOfBirthForm(CompoundDate dob) {
    return DateOfBirthForm.builder().dateOfBirth(dob).build();
  }

  public static HealthConditionsForm getHealthConditionsForm() {
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

  public static LocalAuthorityRefData getLocalAuthorityRefData(
      Nation nation, boolean paymentsEnabled) {
    LocalAuthorityRefData localAuthorityRefData = new LocalAuthorityRefData();
    localAuthorityRefData.setShortCode(Values.LA_SHORT_CODE);
    LocalAuthorityRefData.LocalAuthorityMetaData meta =
        new LocalAuthorityRefData.LocalAuthorityMetaData();
    meta.setNation(nation);
    meta.setPaymentsEnabled(paymentsEnabled);
    meta.setBadgeCost(new BigDecimal("10.00"));
    localAuthorityRefData.setLocalAuthorityMetaData(meta);
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

  public static Journey getDefaultJourneyToStep(StepDefinition step) {
    return getDefaultJourneyToStep(step, EligibilityCodeField.PIP, false);
  }

  public static Journey getDefaultJourneyToStep(
      StepDefinition step, EligibilityCodeField eligibility) {
    return getDefaultJourneyToStep(step, eligibility, SCO, false);
  }

  public static Journey getDefaultJourneyToStep(
      StepDefinition step, EligibilityCodeField eligibility, boolean paymentsEnable) {
    return getDefaultJourneyToStep(step, eligibility, SCO, paymentsEnable);
  }

  public static Journey getDefaultJourneyToStep(
      StepDefinition step,
      EligibilityCodeField eligibility,
      Nation nation,
      boolean paymentsEnable) {
    return getDefaultJourneyToStepWithOptions(
        JourneyBuildOptions.builder()
            .applicantType(Values.APPLICANT_TYPE)
            .dob(Values.DOB_ADULT)
            .step(step)
            .eligibility(eligibility)
            .nation(nation)
            .paymentsEnable(paymentsEnable)
            .build());
  }

  private static Journey addOrganisationJourney(Journey journey, JourneyBuildOptions options) {
    StepDefinition stepTo = options.getStep();
    journey.setFormForStep(OrganisationCareForm.builder().doesCare(Boolean.TRUE).build());
    if (stepTo == StepDefinition.ORGANISATION_CARE) return journey;
    journey.setFormForStep(
        OrganisationTransportForm.builder().doesTransport(options.getOrgDoesCare()).build());
    if (stepTo == StepDefinition.ORGANISATION_TRANSPORT) return journey;
    journey.setFormForStep(new OrganisationMayBeEligibleForm());
    if (stepTo == StepDefinition.ORGANISATION_MAY_BE_ELIGIBLE) return journey;

    return journey;
  }

  public static Journey getDefaultJourneyToStepWithOptions(JourneyBuildOptions options) {
    StepDefinition stepTo = options.getStep();
    ApplicantType applicantType = options.getApplicantType();
    EligibilityCodeField eligibility = options.getEligibility();

    Journey journey = new Journey();

    if (StepDefinition.HOME == stepTo) return journey;

    // Preamble section
    journey.setFormForStep(getApplicantForm(applicantType));
    if (StepDefinition.APPLICANT_TYPE == stepTo) return journey;
    journey.setFormForStep(getFindYourCouncilForm());
    if (StepDefinition.FIND_COUNCIL == stepTo) return journey;
    journey.setFormForStep(getChooseYourCouncilForm());
    if (StepDefinition.CHOOSE_COUNCIL == stepTo) return journey;
    journey.setFormForStep(getYourIssuingAuthorityForm());
    journey.setLocalAuthority(
        getLocalAuthorityRefData(options.getNation(), options.isPaymentsEnable()));
    if (StepDefinition.YOUR_ISSUING_AUTHORITY == stepTo) return journey;
    // Branch off to org journey.
    if (applicantType == ApplicantType.ORGANISATION) {
      return addOrganisationJourney(journey, options);
    }
    journey.setFormForStep(getExistingBadgeForm());
    if (StepDefinition.EXISTING_BADGE == stepTo) return journey;

    // Check Eligibility section
    if (EligibilityCodeField.PIP == eligibility) {
      journey.setFormForStep(ReceiveBenefitsForm.builder().benefitType(PIP).build());
      if (StepDefinition.RECEIVE_BENEFITS == stepTo) return journey;
      journey.setFormForStep(getPipMovingAroundForm());
      if (StepDefinition.PIP_MOVING_AROUND == stepTo) return journey;
      journey.setFormForStep(getPipPlanningJourneyForm());
      if (StepDefinition.PIP_PLANNING_JOURNEY == stepTo) return journey;
      journey.setFormForStep(getPipDlaForm());
      if (StepDefinition.PIP_DLA == stepTo) return journey;
      journey.setFormForStep(new EligibleForm());
      if (StepDefinition.ELIGIBLE == stepTo) return journey;
    }
    if (EligibilityCodeField.WALKD == eligibility) {
      journey.setFormForStep(ReceiveBenefitsForm.builder().benefitType(WALKD).build());
      if (StepDefinition.RECEIVE_BENEFITS == stepTo) return journey;
      journey.setFormForStep(MainReasonForm.builder().mainReasonOption(WALKD).build());
      if (StepDefinition.MAIN_REASON == stepTo) return journey;
      journey.setFormForStep(getWalkingDifficultyForm());
      if (StepDefinition.WALKING_DIFFICULTY == stepTo) return journey;
      journey.setFormForStep(new MayBeEligibleForm());
    }
    if (EligibilityCodeField.CHILDBULK == eligibility) {
      journey.setFormForStep(ReceiveBenefitsForm.builder().benefitType(NONE).build());
      if (StepDefinition.RECEIVE_BENEFITS == stepTo) return journey;
      journey.setFormForStep(MainReasonForm.builder().mainReasonOption(CHILDBULK).build());
      if (StepDefinition.MAIN_REASON == stepTo) return journey;
      journey.setFormForStep(new MayBeEligibleForm());
    }
    if (EligibilityCodeField.BLIND == eligibility) {
      journey.setFormForStep(ReceiveBenefitsForm.builder().benefitType(NONE).build());
      if (StepDefinition.RECEIVE_BENEFITS == stepTo) return journey;
      journey.setFormForStep(MainReasonForm.builder().mainReasonOption(BLIND).build());
      if (StepDefinition.MAIN_REASON == stepTo) return journey;
      journey.setFormForStep(new EligibleForm());
    }
    if (EligibilityCodeField.NONE == eligibility) {
      journey.setFormForStep(ReceiveBenefitsForm.builder().benefitType(NONE).build());
      if (StepDefinition.RECEIVE_BENEFITS == stepTo) return journey;
      journey.setFormForStep(MainReasonForm.builder().mainReasonOption(NONE).build());
      // End of journey - not eligible page
      return journey;
    }
    if (EligibilityCodeField.AFRFCS == eligibility) {
      journey.setFormForStep(ReceiveBenefitsForm.builder().benefitType(AFRFCS).build());
      if (StepDefinition.RECEIVE_BENEFITS == stepTo) return journey;
      journey.setFormForStep(getCompensationSchemeForm());
      if (StepDefinition.AFCS_COMPENSATION_SCHEME == stepTo) return journey;
      journey.setFormForStep(getMentalDisorderForm());
      if (StepDefinition.AFCS_MENTAL_DISORDER == stepTo) return journey;
      journey.setFormForStep(getDisabilityForm());
      if (StepDefinition.AFCS_DISABILITY == stepTo) return journey;
    }
    if (EligibilityCodeField.TERMILL == eligibility) {
      journey.setFormForStep(ReceiveBenefitsForm.builder().benefitType(NONE).build());
      if (StepDefinition.RECEIVE_BENEFITS == stepTo) return journey;
      journey.setFormForStep(MainReasonForm.builder().mainReasonOption(TERMILL).build());
      // End of journey
      return journey;
    }
    if (EligibilityCodeField.DLA == eligibility || stepTo == StepDefinition.HIGHER_RATE_MOBILITY) {
      journey.setFormForStep(ReceiveBenefitsForm.builder().benefitType(DLA).build());
      if (StepDefinition.RECEIVE_BENEFITS == stepTo) return journey;
      journey.setFormForStep(getHightRateMobilityForm());
      if (StepDefinition.HIGHER_RATE_MOBILITY == stepTo) return journey;
      journey.setFormForStep(new EligibleForm());
    }
    if (EligibilityCodeField.WPMS == eligibility) {
      journey.setFormForStep(ReceiveBenefitsForm.builder().benefitType(WPMS).build());
      if (StepDefinition.RECEIVE_BENEFITS == stepTo) return journey;
      journey.setFormForStep(new EligibleForm());
    }
    if (EligibilityCodeField.ARMS == eligibility) {
      journey.setFormForStep(ReceiveBenefitsForm.builder().benefitType(NONE).build());
      journey.setFormForStep(MainReasonForm.builder().mainReasonOption(ARMS).build());
      journey.setFormForStep(new MayBeEligibleForm());
    }
    if (EligibilityCodeField.CHILDVEHIC == eligibility) {
      journey.setFormForStep(ReceiveBenefitsForm.builder().benefitType(NONE).build());
      journey.setFormForStep(MainReasonForm.builder().mainReasonOption(CHILDVEHIC).build());
      journey.setFormForStep(new MayBeEligibleForm());
    }

    // Start application Section
    journey.setFormForStep(getApplicantNameForm());
    if (StepDefinition.NAME == stepTo) return journey;
    journey.setFormForStep(getDateOfBirthForm(options.getDob()));
    if (StepDefinition.DOB == stepTo) return journey;
    journey.setFormForStep(getGenderForm());
    if (StepDefinition.GENDER == stepTo) return journey;
    journey.setFormForStep(getNinoForm());
    if (StepDefinition.NINO == stepTo) return journey;
    journey.setFormForStep(getEnterAddressForm());
    if (StepDefinition.ADDRESS == stepTo) return journey;

    journey.setFormForStep(getContactDetailsForm());

    journey.setFormForStep(getHealthConditionsForm());

    if (PIP == eligibility || DLA == eligibility) {
      journey.setFormForStep(ProveBenefitForm.builder().hasProof(Boolean.TRUE).build());
      if (StepDefinition.UPLOAD_BENEFIT == stepTo) return journey;
      journey.setFormForStep(
          UploadBenefitForm.builder()
              .journeyArtifacts(ImmutableList.of(buildJourneyArtifact("http://s3/benefitLink")))
              .build());
    }

    // Eligibility specific section
    if (WALKD == eligibility) {
      journey.setFormForStep(getWhatMakesWalkingDifficultForm());
      if (StepDefinition.WHAT_MAKES_WALKING_DIFFICULT == stepTo) return journey;
      journey.setFormForStep(getBreathlessnessForm());
      if (StepDefinition.BREATHLESSNESS == stepTo) return journey;
      journey.setFormForStep(getMobilityAidListForm());
      if (StepDefinition.MOBILITY_AID_LIST == stepTo) return journey;
      journey.setFormForStep(getWalkingTimeForm());
      if (StepDefinition.WALKING_TIME == stepTo) return journey;
      journey.setFormForStep(getWhereCanYouWalkForm());
      if (StepDefinition.WHERE_CAN_YOU_WALK == stepTo) return journey;
      journey.setFormForStep(getUploadSupportingDocumentsForm());
      if (StepDefinition.UPLOAD_SUPPORTING_DOCUMENTS == stepTo) return journey;
      journey.setFormForStep(getMedicationListForm());
      if (StepDefinition.MEDICATION_LIST == stepTo) return journey;
      journey.setFormForStep(getTreatmentListForm());
      if (StepDefinition.TREATMENT_LIST == stepTo) return journey;
    }

    if (ARMS == eligibility) {
      journey.setFormForStep(getUploadSupportingDocumentsForm());
      if (StepDefinition.UPLOAD_SUPPORTING_DOCUMENTS == stepTo) return journey;
      journey.setFormForStep(
          ArmsHowOftenDriveForm.builder().howOftenDrive(Values.ARMS_HOW_OFTEN_DRIVE).build());
      if (StepDefinition.ARMS_HOW_OFTEN_DRIVE == stepTo) return journey;
      journey.setFormForStep(
          ArmsAdaptedVehicleForm.builder()
              .hasAdaptedVehicle(Values.IS_ADAPTED_VEHICLE)
              .adaptedVehicleDescription(Values.ARMS_ADAPTED_VEH_DESC)
              .build());
      if (StepDefinition.ARMS_DRIVE_ADAPTED_VEHICLE == stepTo) return journey;
      journey.setFormForStep(
          ArmsDifficultyParkingMetersForm.builder()
              .parkingMetersDifficultyDescription(Values.ARMS_PARKING_METERS)
              .build());
      if (StepDefinition.ARMS_DIFFICULTY_PARKING_METER == stepTo) return journey;
    }

    if (EligibilityCodeField.BLIND == eligibility) {
      if (StepDefinition.CONTACT_DETAILS == stepTo) return journey;

      journey.setFormForStep(RegisteredForm.builder().hasRegistered(true).build());
      if (StepDefinition.REGISTERED == stepTo) return journey;

      journey.setFormForStep(PermissionForm.builder().hasPermission(true).build());
      if (StepDefinition.PERMISSION == stepTo) return journey;

      LocalAuthorityRefData localAuthorityRefData = new LocalAuthorityRefData();
      localAuthorityRefData.setShortCode("WARCC");
      journey.setFormForStep(
          RegisteredCouncilForm.builder()
              .registeredCouncil("WARCC")
              .localAuthorityForRegisteredBlind(localAuthorityRefData)
              .build());
      if (StepDefinition.REGISTERED_COUNCIL == stepTo) return journey;
    }

    if (EligibilityCodeField.CHILDBULK == eligibility) {
      if (StepDefinition.HEALTH_CONDITIONS == stepTo) return journey;
      journey.setFormForStep(getUploadSupportingDocumentsForm());
      if (StepDefinition.UPLOAD_SUPPORTING_DOCUMENTS == stepTo) return journey;
      journey.setFormForStep(getMedicalEquipmentForm());
      if (StepDefinition.MEDICAL_EQUIPMENT == stepTo) return journey;
    }

    journey.setFormForStep(getHealthcareProfessionalListForm());

    journey.setFormForStep(
        ProveIdentityForm.builder()
            .journeyArtifact(buildJourneyArtifact("http://s3/proveIdLink"))
            .build());
    journey.setFormForStep(
        ProveAddressForm.builder()
            .journeyArtifact(buildJourneyArtifact("http://s3/proveAddress"))
            .build());
    journey.setFormForStep(
        ProvidePhotoForm.builder()
            .journeyArtifact(buildJourneyArtifact("http://s3/photoLink"))
            .build());

    journey.setFormForStep(DeclarationSubmitForm.builder().agreed(Boolean.TRUE).build());

    if (options.isPaymentsEnable()) {
      if (StepDefinition.BADGE_PAYMENT == stepTo) return journey;
      journey.setFormForStep(BadgePaymentForm.builder().build());
      journey.setPaymentJourneyUuid("paymentJourneyUuid1");
      if (StepDefinition.BADGE_PAYMENT_RETURN == stepTo) return journey;
      journey.setFormForStep(BadgePaymentReturnForm.builder().build());
      Map<String, String> data;
      if (StepDefinition.NOT_PAID == stepTo) {
        data =
            new HashMap<String, String>() {
              {
                put("paymentJourneyUuid", "paymentJourneyUuid1");
                put("status", "failed");
                put("reference", "paymentReference1");
              }
            };
        journey.setPaymentStatusResponse(PaymentStatusResponse.builder().data(data).build());
        journey.setFormForStep(NotPaidForm.builder().retry("no").build());
      } else {
        if (StepDefinition.PAYMENT_UNAVAILABLE == stepTo) {
          journey.setFormForStep(PaymentUnavailableForm.builder().build());
        } else {
          data =
              new HashMap<String, String>() {
                {
                  put("paymentJourneyUuid", "paymentJourneyUuid1");
                  put("status", "success");
                  put("reference", "paymentReference1");
                }
              };
          journey.setPaymentStatusResponse(PaymentStatusResponse.builder().data(data).build());
        }
      }
    }

    return journey;
  }

  private static UploadSupportingDocumentsForm getUploadSupportingDocumentsForm(
      UploadSupportingDocumentsForm.UploadSupportingDocumentsFormBuilder
          uploadSupportingDocumentsFormBuilder,
      JourneyArtifact build) {
    return uploadSupportingDocumentsFormBuilder.journeyArtifacts(Lists.newArrayList(build)).build();
  }

  @SneakyThrows
  private static JourneyArtifact buildJourneyArtifact(String testUrl) {
    return JourneyArtifact.builder().url(new URL(testUrl)).fileName(testUrl).type("file").build();
  }

  private static StepForm getDisabilityForm() {
    return DisabilityForm.builder().hasDisability(Boolean.TRUE).build();
  }

  public static MedicalEquipmentForm getMedicalEquipmentForm() {
    return MedicalEquipmentForm.builder()
        .equipment(Arrays.asList(PUMP, OTHER))
        .otherDescription("another medical equipment")
        .build();
  }

  public static FindYourCouncilForm getFindYourCouncilForm() {
    return FindYourCouncilForm.builder().postcode(null).build();
  }

  public static ChooseYourCouncilForm getChooseYourCouncilForm() {
    return ChooseYourCouncilForm.builder().councilShortCode("WORCS").build();
  }

  public static PipDlaQuestionForm getPipDlaForm() {
    return PipDlaQuestionForm.builder()
        .receivedDlaOption(PipDlaQuestionForm.PipReceivedDlaOption.HAS_RECEIVED_DLA)
        .build();
  }

  public static RegisteredForm getRegisteredForm() {
    return RegisteredForm.builder().hasRegistered(true).build();
  }

  public static Journey getDefaultJourney() {
    return getDefaultJourneyToStep(null);
  }
}
