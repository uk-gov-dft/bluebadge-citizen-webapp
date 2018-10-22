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
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.component.CompoundDate;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantNameForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantType;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ContactDetailsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.DateOfBirthForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.EnterAddressForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ExistingBadgeForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.GenderForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthConditionsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthcareProfessionalAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthcareProfessionalListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MobilityAidAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MobilityAidListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.NinoForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ReceiveBenefitsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.TreatmentAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.TreatmentListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.WhereCanYouWalkForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.YourIssuingAuthorityForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.MainReasonForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.MedicationAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.MedicationListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.WalkingTimeForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.WhatMakesWalkingDifficultForm;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.AFRFCS;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.CHILDBULK;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.CHILDVEHIC;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.DLA;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.NONE;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.PIP;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.WALKD;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.WPMS;

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

  private static ExistingBadgeForm getExistingBadgeForm() {
    return ExistingBadgeForm.builder()
        .hasExistingBadge(true)
        .badgeNumber(Values.EXISTING_BADGE_NO)
        .build();
  }

  public static HealthcareProfessionalListForm getHealthcareProfessionalListForm() {
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

  private static WalkingTimeForm getWalkingTimeForm() {
    return WalkingTimeForm.builder().walkingTime(Values.WALKING_TIME).build();
  }

  private static WhatMakesWalkingDifficultForm getWhatMakesWalkingDifficultForm() {
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

  private static NinoForm getNinoForm() {
    return NinoForm.builder().nino(Values.NINO).build();
  }

  private static LocalAuthorityRefData getLocalAuthorityRefData() {
    LocalAuthorityRefData localAuthorityRefData = new LocalAuthorityRefData();
    localAuthorityRefData.setShortCode(Values.LA_SHORT_CODE);
    return localAuthorityRefData;
  }

  public static Journey getDefaultJourneyToStep(StepDefinition step) {
    Journey journey = new Journey();

    journey.setApplicantForm(getApplicantForm());
    if (StepDefinition.APPLICANT_TYPE == step) return journey;
    journey.setYourIssuingAuthorityForm(getYourIssuingAuthorityForm());
    journey.setLocalAuthority(getLocalAuthorityRefData());
    if (StepDefinition.YOUR_ISSUING_AUTHORITY == step) return journey;

    journey.setEnterAddressForm(getEnterAddressForm());

    journey.setHealthConditionsForm(getHealthConditionsForm());
    journey.setApplicantNameForm(getApplicantNameForm());
    journey.setDateOfBirthForm(getDateOfBirthForm());
    journey.setGenderForm(getGenderForm());
    journey.setReceiveBenefitsForm(ReceiveBenefitsForm.builder().benefitType(WALKD).build());
    journey.setWhereCanYouWalkForm(getWhereCanYouWalkForm());
    journey.setContactDetailsForm(getContactDetailsForm());
    journey.setWhatMakesWalkingDifficultForm(getWhatMakesWalkingDifficultForm());
    journey.setHealthcareProfessionalListForm(getHealthcareProfessionalListForm());
    journey.setWalkingTimeForm(getWalkingTimeForm());

    return journey;
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
    when(mockJourney.getLocalAuthority()).thenReturn(getLocalAuthorityRefData());
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
    when(mockJourney.getFormForStep(StepDefinition.EXISTING_BADGE)).thenReturn(getExistingBadgeForm());
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
      return setNation(Nation.SCO);
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
