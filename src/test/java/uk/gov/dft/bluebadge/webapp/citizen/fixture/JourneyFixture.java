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
import uk.gov.dft.bluebadge.webapp.citizen.model.form.GenderForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthConditionsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MobilityAidAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MobilityAidListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.NinoForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ReceiveBenefitsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.TreatmentAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.TreatmentListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.WhereCanYouWalkForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.YourIssuingAuthorityForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.MedicationAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.MedicationListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.WalkingTimeForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.WhatMakesWalkingDifficultForm;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class JourneyFixture {

  public interface Values {
    String TREATMENT_DESCRIPTION = "Desc";
    String TREATMENT_WHEN = "When";

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
    String POSTCODE = "BR4 9NA";
    String PRIMARY_PHONE_NO = "01270646362";
    String WHERE_WALK_DESTINATION = "London";
    String WHERE_WALK_TIME = "10 minutes";
    String WHAT_WALKING_SOME_ELSE_DESC = "Some description of walking";
    String DOB_AS_EQUAL_TO_STRING = "1990-01-01";
  }

  public static TreatmentListForm treatmentListForm;
  public static MedicationListForm medicationListForm;
  public static MobilityAidListForm mobilityAidListForm;
  private static WalkingTimeForm walkingTimeForm =
      WalkingTimeForm.builder().walkingTime(Values.WALKING_TIME).build();
  private static WhatMakesWalkingDifficultForm whatMakesWalkingDifficultForm =
      WhatMakesWalkingDifficultForm.builder()
          .whatWalkingDifficulties(Values.WHAT_MAKES_WALKING_DIFFICULT)
          .somethingElseDescription(Values.WHAT_WALKING_SOME_ELSE_DESC)
          .build();
  private static WhereCanYouWalkForm whereCanYouWalkForm =
      WhereCanYouWalkForm.builder()
          .destinationToHome(Values.WHERE_WALK_DESTINATION)
          .timeToDestination(Values.WHERE_WALK_TIME)
          .build();
  private static ApplicantNameForm applicantNameForm =
      ApplicantNameForm.builder()
          .fullName(Values.FULL_NAME)
          .hasBirthName(true)
          .birthName(Values.BIRTH_NAME)
          .build();
  private static GenderForm genderForm = GenderForm.builder().gender(Values.GENDER).build();
  private static ApplicantForm applicantForm =
      ApplicantForm.builder().applicantType(Values.APPLICANT_TYPE).build();
  private static YourIssuingAuthorityForm yourIssuingAuthorityForm =
      YourIssuingAuthorityForm.builder().localAuthorityShortCode(Values.LA_SHORT_CODE).build();
  private static DateOfBirthForm dateOfBirthForm =
      DateOfBirthForm.builder().dateOfBirth(new CompoundDate("1", "1", "1990")).build();

  private static HealthConditionsForm healthConditionsForm =
      HealthConditionsForm.builder()
          .descriptionOfConditions(Values.DESCRIPTION_OF_CONDITIONS)
          .build();
  private static EnterAddressForm enterAddressForm =
      EnterAddressForm.builder()
          .buildingAndStreet(Values.ADDRESS_LINE_1)
          .optionalAddress(Values.ADDRESS_LINE_2)
          .townOrCity(Values.TOWN_OR_CITY)
          .postcode(Values.POSTCODE)
          .build();
  private static ContactDetailsForm contactDetailsForm =
      ContactDetailsForm.builder().primaryPhoneNumber(Values.PRIMARY_PHONE_NO).build();
  private static NinoForm ninoForm = NinoForm.builder().nino(Values.NINO).build();
  private static LocalAuthorityRefData localAuthorityRefData;

  static {
    localAuthorityRefData = new LocalAuthorityRefData();
    localAuthorityRefData.setShortCode(Values.LA_SHORT_CODE);

    MobilityAidAddForm mobilityAidAddForm = new MobilityAidAddForm();
    mobilityAidAddForm.setHowProvidedCodeField(Values.MOBILITY_HOW_PROVIDED);
    mobilityAidAddForm.setAidType(Values.MOBILITY_AID_TYPE);
    mobilityAidAddForm.setUsage(Values.MOBILITY_USAGE);

    mobilityAidListForm =
        MobilityAidListForm.builder()
            .hasWalkingAid(Values.YES)
            .mobilityAids(Lists.newArrayList(mobilityAidAddForm))
            .build();

    TreatmentAddForm treatmentAddForm = new TreatmentAddForm();
    treatmentAddForm.setTreatmentDescription(Values.TREATMENT_DESCRIPTION);
    treatmentAddForm.setTreatmentWhen(Values.TREATMENT_WHEN);

    treatmentListForm =
        TreatmentListForm.builder()
            .hasTreatment(Values.YES)
            .treatments(Lists.newArrayList(treatmentAddForm))
            .build();

    MedicationAddForm medicationAddForm = new MedicationAddForm();
    medicationAddForm.setPrescribed(Values.YES);
    medicationAddForm.setName(Values.MEDICATION_NAME);
    medicationAddForm.setFrequency(Values.MEDICATION_FREQUENCY);
    medicationAddForm.setDosage(Values.MEDICATION_DOSAGE);

    medicationListForm =
        MedicationListForm.builder()
            .hasMedication(Values.YES)
            .medications(Lists.newArrayList(medicationAddForm))
            .build();
  }

  public static Journey getDefaultJourneyToStep(StepDefinition step) {
    Journey journey = new Journey();

    journey.setApplicantForm(applicantForm);
    if (StepDefinition.APPLICANT_TYPE == step) return journey;
    journey.setYourIssuingAuthorityForm(yourIssuingAuthorityForm);
    journey.setLocalAuthority(localAuthorityRefData);
    if (StepDefinition.YOUR_ISSUING_AUTHORITY == step) return journey;

    journey.setEnterAddressForm(enterAddressForm);

    journey.setHealthConditionsForm(healthConditionsForm);
    journey.setApplicantNameForm(applicantNameForm);
    journey.setDateOfBirthForm(dateOfBirthForm);
    journey.setGenderForm(genderForm);
    journey.setReceiveBenefitsForm(
        ReceiveBenefitsForm.builder().benefitType(EligibilityCodeField.WALKD).build());
    journey.setWhereCanYouWalkForm(whereCanYouWalkForm);
    journey.setContactDetailsForm(contactDetailsForm);
    journey.setWhatMakesWalkingDifficultForm(whatMakesWalkingDifficultForm);
    journey.setWalkingTimeForm(walkingTimeForm);

    return journey;
  }

  public static Journey getDefaultJourney() {
    return getDefaultJourneyToStep(null);
  }

  public static void configureMockJourney(Journey mockJourney) {
    when(mockJourney.getFormForStep(StepDefinition.NAME)).thenReturn(applicantNameForm);
    when(mockJourney.getFormForStep(StepDefinition.GENDER)).thenReturn(genderForm);
    when(mockJourney.getFormForStep(StepDefinition.NINO)).thenReturn(ninoForm);
    when(mockJourney.getFormForStep(StepDefinition.DOB)).thenReturn(dateOfBirthForm);
    when(mockJourney.getFormForStep(StepDefinition.TREATMENT_LIST)).thenReturn(treatmentListForm);
    when(mockJourney.getFormForStep(StepDefinition.MEDICATION_LIST)).thenReturn(medicationListForm);
    when(mockJourney.getFormForStep(StepDefinition.MOBILITY_AID_LIST))
        .thenReturn(mobilityAidListForm);
    when(mockJourney.getFormForStep(StepDefinition.WALKING_TIME)).thenReturn(walkingTimeForm);
    when(mockJourney.getFormForStep(StepDefinition.WHAT_WALKING_DIFFICULTIES))
        .thenReturn(whatMakesWalkingDifficultForm);
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
