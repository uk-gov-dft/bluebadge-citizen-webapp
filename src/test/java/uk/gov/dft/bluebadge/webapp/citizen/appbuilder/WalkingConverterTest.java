package uk.gov.dft.bluebadge.webapp.citizen.appbuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.WALKD;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingDifficultyTypeCodeField.PAIN;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.EnumSet;
import java.util.List;
import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Breathlessness;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.BreathlessnessCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.HowProvidedCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Medication;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Treatment;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingAid;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingDifficulty;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingDifficultyTypeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingLengthOfTimeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyBuilder;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MobilityAidListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.TreatmentListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.BreathlessnessForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.MedicationListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.WhatMakesWalkingDifficultForm;

public class WalkingConverterTest {

  private static final Breathlessness BREATHLESSNESS_RESULTS =
      Breathlessness.builder()
          .typeCodes(
              Lists.newArrayList(BreathlessnessCodeField.UPHILL, BreathlessnessCodeField.OTHER))
          .otherDescription("Some description of breathlessness")
          .build();

  @Test
  public void convert() {

    WalkingDifficulty result =
        WalkingConverter.convert(new JourneyBuilder().withEligibility(WALKD).build());
    // Detail covered in other tests.
    assertThat(result.getTypeCodes().size()).isEqualTo(3);
    assertThat(result.getWalkingLengthOfTimeCode()).isEqualTo(WalkingLengthOfTimeCodeField.LESSMIN);
  }

  @Test
  public void convert_withBreathlessness() {
    Journey journey = new JourneyBuilder().withEligibility(WALKD).build();
    journey.setFormForStep(JourneyFixture.getBreathlessnessForm());

    WalkingDifficulty result = WalkingConverter.convert(journey);
    // Detail covered in other tests.
    assertThat(result.getBreathlessness()).isEqualTo(BREATHLESSNESS_RESULTS);
  }

  @Test
  public void convert_withBreathlessness_withOtherDescButNotOther() {
    Journey journey = new JourneyBuilder().withEligibility(WALKD).build();
    BreathlessnessForm breathlessnessForm = JourneyFixture.getBreathlessnessForm();
    breathlessnessForm.setBreathlessnessTypes(ImmutableList.of(BreathlessnessCodeField.OWNPACE));
    breathlessnessForm.setBreathlessnessOtherDescription("something");
    journey.setFormForStep(breathlessnessForm);

    WalkingDifficulty result = WalkingConverter.convert(journey);
    // Detail covered in other tests.
    assertThat(result.getBreathlessness()).isNotNull();
    assertThat(result.getBreathlessness().getOtherDescription()).isNull();
    assertThat(result.getBreathlessness().getTypeCodes())
        .containsExactly(BreathlessnessCodeField.OWNPACE);
  }

  @Test
  public void convert_withBreathlessnessFormButNotBreathlessnessDifficulty() {
    Journey journey = new JourneyBuilder().withEligibility(WALKD).build();
    WhatMakesWalkingDifficultForm whatMakesWalkingDifficultForm =
        JourneyFixture.getWhatMakesWalkingDifficultForm();
    whatMakesWalkingDifficultForm.setWhatWalkingDifficulties(ImmutableList.of(PAIN));
    journey.setFormForStep(whatMakesWalkingDifficultForm);
    journey.setFormForStep(JourneyFixture.getBreathlessnessForm());

    WalkingDifficulty result = WalkingConverter.convert(journey);
    // Detail covered in other tests.
    assertThat(result.getBreathlessness()).isNull();
  }

  @Test
  public void setWalkingExtraQuestions() {
    EnumSet<WalkingDifficultyTypeCodeField> allTypes =
        EnumSet.allOf(WalkingDifficultyTypeCodeField.class);
    WhatMakesWalkingDifficultForm form =
        WhatMakesWalkingDifficultForm.builder()
            .whatWalkingDifficulties(Lists.newArrayList(allTypes))
            .painDescription("pain")
            .balanceDescription("balance")
            .healthProfessionsForFalls(true)
            .dangerousDescription("danger")
            .chestLungHeartEpilepsy(true)
            .somethingElseDescription("X")
            .build();

    WalkingDifficulty.WalkingDifficultyBuilder builder = WalkingDifficulty.builder();
    WalkingConverter.setWalkingExtraQuestions(builder, form);
    WalkingDifficulty walkingDifficulty = builder.build();

    assertThat(walkingDifficulty.getPainDescription()).isEqualTo("pain");
    assertThat(walkingDifficulty.getBalanceDescription()).isEqualTo("balance");
    assertThat(walkingDifficulty.getHealthProfessionsForFalls()).isEqualTo(true);
    assertThat(walkingDifficulty.getDangerousDescription()).isEqualTo("danger");
    assertThat(walkingDifficulty.getChestLungHeartEpilepsy()).isEqualTo(true);
  }

  @Test
  public void setWalkingExtraQuestions_notExtraType() {
    WhatMakesWalkingDifficultForm form =
        WhatMakesWalkingDifficultForm.builder()
            .whatWalkingDifficulties(Lists.newArrayList(WalkingDifficultyTypeCodeField.BREATH))
            .painDescription("pain")
            .balanceDescription("balance")
            .healthProfessionsForFalls(true)
            .dangerousDescription("danger")
            .chestLungHeartEpilepsy(true)
            .somethingElseDescription("X")
            .build();

    WalkingDifficulty.WalkingDifficultyBuilder builder = WalkingDifficulty.builder();
    WalkingConverter.setWalkingExtraQuestions(builder, form);
    WalkingDifficulty walkingDifficulty = builder.build();

    assertThat(walkingDifficulty.getPainDescription()).isNull();
    assertThat(walkingDifficulty.getBalanceDescription()).isNull();
    assertThat(walkingDifficulty.getHealthProfessionsForFalls()).isNull();
    assertThat(walkingDifficulty.getDangerousDescription()).isNull();
    assertThat(walkingDifficulty.getChestLungHeartEpilepsy()).isNull();
  }

  @Test
  public void setWalkingExtraQuestions_otherDesc() {
    WalkingDifficulty.WalkingDifficultyBuilder builder = WalkingDifficulty.builder();

    WalkingConverter.setWalkingExtraQuestions(
        builder,
        WhatMakesWalkingDifficultForm.builder()
            .whatWalkingDifficulties(Lists.newArrayList(WalkingDifficultyTypeCodeField.SOMELSE))
            .somethingElseDescription("X")
            .build());
    assertThat(builder.build().getOtherDescription()).isEqualTo("X");

    EnumSet.complementOf(EnumSet.of(WalkingDifficultyTypeCodeField.SOMELSE))
        .forEach(
            i -> {
              WalkingDifficulty.WalkingDifficultyBuilder tmp = WalkingDifficulty.builder();
              WalkingConverter.setWalkingExtraQuestions(
                  tmp,
                  WhatMakesWalkingDifficultForm.builder()
                      .whatWalkingDifficulties(Lists.newArrayList(i))
                      .somethingElseDescription("X")
                      .build());
              assertThat(tmp.build().getOtherDescription()).isNull();
            });
  }

  @Test
  public void getWalkingAids_withOne() {
    List<WalkingAid> result =
        WalkingConverter.getWalkingAids(JourneyFixture.getMobilityAidListForm());
    assertThat(result.get(0).getDescription()).isEqualTo("Scooter");
    assertThat(result.get(0).getUsage()).isEqualTo("Usage");
    assertThat(result.get(0).getHowProvidedCode()).isEqualTo(HowProvidedCodeField.PRESCRIBE);
  }

  @Test
  public void getTreatments_withOne() {
    List<Treatment> result = WalkingConverter.getTreatments(JourneyFixture.getTreatmentListForm());
    assertThat(result.get(0).getDescription()).isEqualTo("Desc");
    assertThat(result.get(0).getTime()).isEqualTo("When");
  }

  @Test
  public void getMedications_withOne() {
    List<Medication> result =
        WalkingConverter.getMedications(JourneyFixture.getMedicationListForm());
    assertThat(result.get(0).getFrequency()).isEqualTo("Frequency");
    assertThat(result.get(0).getIsPrescribed()).isEqualTo(Boolean.TRUE);
    assertThat(result.get(0).getName()).isEqualTo("name");
    assertThat(result.get(0).getQuantity()).isEqualTo("Dosage");
  }

  @Test
  public void getBreathlessness_withOtherDescription() {
    Breathlessness result =
        WalkingConverter.getBreathlessness(
            JourneyFixture.getWhatMakesWalkingDifficultForm(),
            JourneyFixture.getBreathlessnessForm());
    assertThat(result).isEqualTo(BREATHLESSNESS_RESULTS);
  }

  @Test
  public void getBreathlessness_withNull() {
    Breathlessness result =
        WalkingConverter.getBreathlessness(JourneyFixture.getWhatMakesWalkingDifficultForm(), null);
    assertThat(result).isNull();
  }

  @Test
  public void getLists_null() {
    assertThat(
            WalkingConverter.getMedications(
                MedicationListForm.builder().hasMedication("no").build()))
        .isNull();
    assertThat(
            WalkingConverter.getWalkingAids(
                MobilityAidListForm.builder().hasWalkingAid("no").build()))
        .isNull();
    assertThat(
            WalkingConverter.getTreatments(TreatmentListForm.builder().hasTreatment("no").build()))
        .isNull();
  }
}
