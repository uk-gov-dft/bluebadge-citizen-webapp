package uk.gov.dft.bluebadge.webapp.citizen.appbuilder;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.HowProvidedCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Medication;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Treatment;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingAid;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingDifficulty;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingDifficultyTypeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingLengthOfTimeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingSpeedCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MobilityAidAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MobilityAidListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.TreatmentListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.MedicationListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.WalkingTimeForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.WhatMakesWalkingDifficultForm;

import java.util.EnumSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class WalkingConverterTest {

  @Mock Journey journey;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    JourneyFixture.configureMockJourney(journey);
  }

  @Test
  public void convert() {

    WalkingDifficulty result = WalkingConverter.convert(journey);
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
  public void getWalkingSpeed() {
    // If cant walk null else slow
    EnumSet<WalkingLengthOfTimeCodeField> cant = EnumSet.of(WalkingLengthOfTimeCodeField.CANTWALK);

    cant.forEach(
        i ->
            assertThat(
                    WalkingConverter.getWalkingSpeed(
                        WalkingTimeForm.builder().walkingTime(i).build()))
                .isNull());

    EnumSet.complementOf(cant)
        .forEach(
            i ->
                assertThat(
                        WalkingConverter.getWalkingSpeed(
                            WalkingTimeForm.builder().walkingTime(i).build()))
                    .isEqualTo(WalkingSpeedCodeField.SLOW));
  }

  @Test
  public void getWalkingAids_withOne() {
    List<WalkingAid> result =
        WalkingConverter.getWalkingAids(JourneyFixture.mobilityAidListForm);
    assertThat(result.get(0).getDescription())
        .isEqualTo(MobilityAidAddForm.AidType.SCOOTER.getType());
    assertThat(result.get(0).getUsage()).isEqualTo("Usage");
    assertThat(result.get(0).getHowProvidedCode()).isEqualTo(HowProvidedCodeField.PRESCRIBE);
  }

  @Test
  public void getTreatments_withOne() {
    List<Treatment> result =
        WalkingConverter.getTreatments(JourneyFixture.treatmentListForm);
    assertThat(result.get(0).getDescription()).isEqualTo("Desc");
    assertThat(result.get(0).getTime()).isEqualTo("When");
  }

  @Test
  public void getMedications_withOne() {
    List<Medication> result =
        WalkingConverter.getMedications(JourneyFixture.medicationListForm);
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
