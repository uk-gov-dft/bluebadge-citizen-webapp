package uk.gov.dft.bluebadge.webapp.citizen.appbuilder;

import java.util.ArrayList;
import java.util.List;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Breathlessness;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Medication;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Treatment;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingAid;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingDifficulty;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingDifficultyTypeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MobilityAidListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.TreatmentListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.BreathlessnessForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.MedicationListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.WalkingTimeForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.WhatMakesWalkingDifficultForm;

class WalkingConverter {

  private WalkingConverter() {}

  static WalkingDifficulty convert(Journey journey) {

    WalkingTimeForm walkingTimeForm = journey.getFormForStep(StepDefinition.WALKING_TIME);
    WhatMakesWalkingDifficultForm whatMakesWalkingDifficultForm =
        journey.getFormForStep(StepDefinition.WHAT_MAKES_WALKING_DIFFICULT);
    MobilityAidListForm mobilityAidListForm =
        journey.getFormForStep(StepDefinition.MOBILITY_AID_LIST);
    TreatmentListForm treatmentListForm = journey.getFormForStep(StepDefinition.TREATMENT_LIST);
    MedicationListForm medicationListForm = journey.getFormForStep(StepDefinition.MEDICATION_LIST);
    BreathlessnessForm breathlessnessForm = journey.getFormForStep(StepDefinition.BREATHLESSNESS);

    WalkingDifficulty.WalkingDifficultyBuilder walkingDifficultyBuilder =
        WalkingDifficulty.builder()
            .walkingLengthOfTimeCode(walkingTimeForm.getWalkingTime())
            .typeCodes(whatMakesWalkingDifficultForm.getWhatWalkingDifficulties());

    setWalkingExtraQuestions(walkingDifficultyBuilder, whatMakesWalkingDifficultForm);
    return walkingDifficultyBuilder
        .walkingAids(getWalkingAids(mobilityAidListForm))
        .treatments(getTreatments(treatmentListForm))
        .medications(getMedications(medicationListForm))
        .breathlessness(getBreathlessness(whatMakesWalkingDifficultForm, breathlessnessForm))
        .build();
  }

  static void setWalkingExtraQuestions(
      WalkingDifficulty.WalkingDifficultyBuilder walkingDifficultyBuilder,
      WhatMakesWalkingDifficultForm whatMakesWalkingDifficultForm) {
    List<WalkingDifficultyTypeCodeField> types =
        whatMakesWalkingDifficultForm.getWhatWalkingDifficulties();
    if (types.contains(WalkingDifficultyTypeCodeField.PAIN)) {
      walkingDifficultyBuilder.painDescription(whatMakesWalkingDifficultForm.getPainDescription());
    }
    if (types.contains(WalkingDifficultyTypeCodeField.BALANCE)) {
      walkingDifficultyBuilder.balanceDescription(
          whatMakesWalkingDifficultForm.getBalanceDescription());
      walkingDifficultyBuilder.healthProfessionsForFalls(
          whatMakesWalkingDifficultForm.getHealthProfessionsForFalls());
    }
    if (types.contains(WalkingDifficultyTypeCodeField.DANGER)) {
      walkingDifficultyBuilder.dangerousDescription(
          whatMakesWalkingDifficultForm.getDangerousDescription());
      walkingDifficultyBuilder.chestLungHeartEpilepsy(
          whatMakesWalkingDifficultForm.getChestLungHeartEpilepsy());
    }
    if (types.contains(WalkingDifficultyTypeCodeField.SOMELSE)) {
      walkingDifficultyBuilder.otherDescription(
          whatMakesWalkingDifficultForm.getSomethingElseDescription());
    }
  }

  @SuppressWarnings("squid:S1168")
  static List<WalkingAid> getWalkingAids(MobilityAidListForm mobilityAidListForm) {

    if (null != mobilityAidListForm && "yes".equals(mobilityAidListForm.getHasWalkingAid())) {
      List<WalkingAid> walkingAids = new ArrayList<>();
      mobilityAidListForm
          .getMobilityAids()
          .forEach(
              i ->
                  walkingAids.add(
                      WalkingAid.builder()
                          .usage(i.getUsage())
                          .howProvidedCode(i.getHowProvidedCodeField())
                          .description(i.getAidType())
                          .build()));
      return walkingAids;
    }
    return null;
  }

  @SuppressWarnings("squid:S1168")
  static List<Treatment> getTreatments(TreatmentListForm treatmentListForm) {

    if (null != treatmentListForm && "yes".equals(treatmentListForm.getHasTreatment())) {
      List<Treatment> treatments = new ArrayList<>();
      treatmentListForm
          .getTreatments()
          .forEach(
              i ->
                  treatments.add(
                      Treatment.builder()
                          .time(i.getTreatmentWhen())
                          .description(i.getTreatmentDescription())
                          .build()));
      return treatments;
    }
    return null;
  }

  @SuppressWarnings("squid:S1168")
  static List<Medication> getMedications(MedicationListForm medicationListForm) {

    if (null != medicationListForm && "yes".equals(medicationListForm.getHasMedication())) {
      List<Medication> medications = new ArrayList<>();
      medicationListForm
          .getMedications()
          .forEach(
              i ->
                  medications.add(
                      Medication.builder()
                          .name(i.getName())
                          .quantity(i.getDosage())
                          .isPrescribed(i.getPrescribedValue())
                          .frequency(i.getFrequency())
                          .build()));
      return medications;
    }
    return null;
  }

  @SuppressWarnings("squid:S1168")
  static Breathlessness getBreathlessness(
      WhatMakesWalkingDifficultForm whatMakesWalkingDifficultForm,
      BreathlessnessForm breathlessnessForm) {

    if (whatMakesWalkingDifficultForm
            .getWhatWalkingDifficulties()
            .contains(WalkingDifficultyTypeCodeField.BREATH)
        && null != breathlessnessForm) {

      return Breathlessness.builder()
          .typeCodes(breathlessnessForm.getBreathlessnessTypes())
          .otherDescription(breathlessnessForm.getBreathlessnessOtherDescription())
          .build();
    }
    return null;
  }
}
