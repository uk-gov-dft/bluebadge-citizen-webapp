package uk.gov.dft.bluebadge.webapp.citizen.appbuilder;

import com.google.common.collect.Lists;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.GenderCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.HowProvidedCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingDifficultyTypeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingLengthOfTimeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.component.CompoundDate;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantNameForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.DateOfBirthForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.GenderForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MobilityAidAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MobilityAidListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.NinoForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.TreatmentAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.TreatmentListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.MedicationAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.MedicationListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.WalkingTimeForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.WhatMakesWalkingDifficultForm;

import java.util.List;

import static org.mockito.Mockito.when;
import static uk.gov.dft.bluebadge.webapp.citizen.appbuilder.ConverterJourneyFixture.Values.TREATMENT_DESCRIPTION;
import static uk.gov.dft.bluebadge.webapp.citizen.appbuilder.ConverterJourneyFixture.Values.TREATMENT_WHEN;

public class ConverterJourneyFixture {

  interface Values{
    String TREATMENT_DESCRIPTION = "Desc";
    String TREATMENT_WHEN = "When";

    String YES = "yes";
    String ID = "1234";
    String MEDICATION_NAME = "name";
    String MEDICATION_FREQUENCY = "Frequency";
    String MEDICATION_DOSAGE = "Dosage";
    String MOBILITY_USAGE = "Usage";
    HowProvidedCodeField MOBILITY_HOW_PROVIDED = HowProvidedCodeField.PRESCRIBE;
    MobilityAidAddForm.AidType MOBILITY_AID_TYPE = MobilityAidAddForm.AidType.SCOOTER;
    WalkingLengthOfTimeCodeField WALKING_TIME = WalkingLengthOfTimeCodeField.LESSMIN;
    List<WalkingDifficultyTypeCodeField> WHAT_MAKES_WALKING_DIFFICULT = Lists.newArrayList(
        WalkingDifficultyTypeCodeField.PAIN, WalkingDifficultyTypeCodeField.BALANCE);
    String BIRTH_NAME = "Birth";
    String FULL_NAME = "Full";
    GenderCodeField GENDER = GenderCodeField.FEMALE;
    String NINO = "NS123456D";
  }

  static TreatmentListForm treatmentListForm;
  static MedicationListForm medicationListForm;
  static MobilityAidListForm mobilityAidListForm;

  static void configureMockJourney(Journey mockJourney){

    WalkingTimeForm walkingTimeForm;
    WhatMakesWalkingDifficultForm whatMakesWalkingDifficultForm;
    TreatmentAddForm treatmentAddForm = new TreatmentAddForm();
    treatmentAddForm.setTreatmentDescription(TREATMENT_DESCRIPTION);
    treatmentAddForm.setTreatmentWhen(TREATMENT_WHEN);

    treatmentListForm =
        TreatmentListForm.builder()
            .hasTreatment(Values.YES)
            .treatments(Lists.newArrayList(treatmentAddForm))
            .build();

    MedicationAddForm medicationAddForm = new MedicationAddForm();
    medicationAddForm.setId(Values.ID);
    medicationAddForm.setPrescribed(Values.YES);
    medicationAddForm.setName(Values.MEDICATION_NAME);
    medicationAddForm.setFrequency(Values.MEDICATION_FREQUENCY);
    medicationAddForm.setDosage(Values.MEDICATION_DOSAGE);

    medicationListForm =
        MedicationListForm.builder()
            .hasMedication(Values.YES)
            .medications(Lists.newArrayList(medicationAddForm))
            .build();

    MobilityAidAddForm mobilityAidAddForm = new MobilityAidAddForm();
    mobilityAidAddForm.setHowProvidedCodeField(Values.MOBILITY_HOW_PROVIDED);
    mobilityAidAddForm.setAidType(Values.MOBILITY_AID_TYPE);
    mobilityAidAddForm.setUsage(Values.MOBILITY_USAGE);

    mobilityAidListForm =
        MobilityAidListForm.builder()
            .hasWalkingAid(Values.YES)
            .mobilityAids(Lists.newArrayList(mobilityAidAddForm))
            .build();

    walkingTimeForm =
        WalkingTimeForm.builder().walkingTime(Values.WALKING_TIME).build();

    whatMakesWalkingDifficultForm =
        WhatMakesWalkingDifficultForm.builder()
            .whatWalkingDifficulties(
                Values.WHAT_MAKES_WALKING_DIFFICULT)
            .build();
    ApplicantNameForm applicantNameForm =
        ApplicantNameForm.builder()
            .birthName(Values.BIRTH_NAME)
            .fullName(Values.FULL_NAME)
            .hasBirthName(Boolean.TRUE)
            .build();
    GenderForm genderForm = GenderForm.builder().gender(Values.GENDER).build();
    NinoForm ninoForm = NinoForm.builder().nino(Values.NINO).build();
    DateOfBirthForm birthForm =
        DateOfBirthForm.builder().dateOfBirth(new CompoundDate("29", "5", "1970")).build();

    when(mockJourney.getFormForStep(StepDefinition.NAME)).thenReturn(applicantNameForm);
    when(mockJourney.getFormForStep(StepDefinition.GENDER)).thenReturn(genderForm);
    when(mockJourney.getFormForStep(StepDefinition.NINO)).thenReturn(ninoForm);
    when(mockJourney.getFormForStep(StepDefinition.DOB)).thenReturn(birthForm);
    when(mockJourney.getFormForStep(StepDefinition.TREATMENT_LIST)).thenReturn(treatmentListForm);
    when(mockJourney.getFormForStep(StepDefinition.MEDICATION_LIST)).thenReturn(medicationListForm);
    when(mockJourney.getFormForStep(StepDefinition.MOBILITY_AID_LIST)).thenReturn(mobilityAidListForm);
    when(mockJourney.getFormForStep(StepDefinition.WALKING_TIME)).thenReturn(walkingTimeForm);
    when(mockJourney.getFormForStep(StepDefinition.WHAT_WALKING_DIFFICULTIES))
        .thenReturn(whatMakesWalkingDifficultForm);
  }
}
