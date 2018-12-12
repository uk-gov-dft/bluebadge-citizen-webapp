package uk.gov.dft.bluebadge.webapp.citizen.appbuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.WALKD;

import com.google.common.collect.Lists;
import java.util.EnumSet;
import java.util.List;
import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.HowProvidedCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Medication;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Treatment;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingAid;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingDifficulty;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingDifficultyTypeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingLengthOfTimeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyBuilder;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MobilityAidListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.TreatmentListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.MedicationListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.WhatMakesWalkingDifficultForm;

public class WalkingConverterTest {

  @Test
  public void convert() {

    WalkingDifficulty result =
        WalkingConverter.convert(new JourneyBuilder().withEligibility(WALKD).build());
    // Detail covered in other tests.
    assertThat(result.getTypeCodes().size()).isEqualTo(2);
    assertThat(result.getWalkingLengthOfTimeCode()).isEqualTo(WalkingLengthOfTimeCodeField.LESSMIN);
  }

  @Test
  public void getOtherDesc() {
    EnumSet<WalkingDifficultyTypeCodeField> someelse =
        EnumSet.of(WalkingDifficultyTypeCodeField.SOMELSE);

    someelse.forEach(
        i ->
            assertThat(
                    WalkingConverter.getOtherDesc(
                        WhatMakesWalkingDifficultForm.builder()
                            .whatWalkingDifficulties(Lists.newArrayList(i))
                            .somethingElseDescription("X")
                            .build()))
                .isEqualTo("X"));

    EnumSet.complementOf(someelse)
        .forEach(
            i ->
                assertThat(
                        WalkingConverter.getOtherDesc(
                            WhatMakesWalkingDifficultForm.builder()
                                .whatWalkingDifficulties(Lists.newArrayList(i))
                                .somethingElseDescription("X")
                                .build()))
                    .isNull());
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
