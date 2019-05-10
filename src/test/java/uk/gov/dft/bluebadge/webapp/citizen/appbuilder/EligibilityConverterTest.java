package uk.gov.dft.bluebadge.webapp.citizen.appbuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.ARMS;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.BLIND;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.CHILDBULK;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.CHILDVEHIC;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.NONE;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.TERMILL;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.WALKD;

import java.util.EnumSet;
import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Eligibility;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyBuilder;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.arms.ArmsAdaptedVehicleForm;

public class EligibilityConverterTest {

  @Test
  public void convert_arms() {
    Eligibility eligibility =
        EligibilityConverter.convert(
            JourneyFixture.getDefaultJourneyToStep(StepDefinition.DECLARATIONS, ARMS));
    assertThat(eligibility.getDisabilityArms()).isNotNull();
    assertThat(eligibility.getDisabilityArms().getAdaptedVehicleDescription())
        .isEqualTo(JourneyFixture.Values.ARMS_ADAPTED_VEH_DESC);
    assertThat(eligibility.getDisabilityArms().getDrivingFrequency())
        .isEqualTo(JourneyFixture.Values.ARMS_HOW_OFTEN_DRIVE);

    // Arms null for other types...
    EnumSet<EligibilityCodeField> notWalking =
        EnumSet.complementOf(EnumSet.of(ARMS, TERMILL, NONE));
    notWalking.forEach(
        i -> {
          Eligibility eli =
              EligibilityConverter.convert(
                  JourneyFixture.getDefaultJourneyToStep(StepDefinition.DECLARATIONS, i));
          assertThat(eli.getDisabilityArms()).isNull();
        });
  }

  @Test
  public void convert_arms_whenNoAdaptedVehButHasADescription() {
    Journey journey = JourneyFixture.getDefaultJourneyToStep(StepDefinition.DECLARATIONS, ARMS);
    ArmsAdaptedVehicleForm form = journey.getFormForStep(StepDefinition.ARMS_DRIVE_ADAPTED_VEHICLE);
    form.setAdaptedVehicleDescription("Some description");
    form.setHasAdaptedVehicle(false);

    Eligibility eligibility = EligibilityConverter.convert(journey);

    assertThat(eligibility.getDisabilityArms()).isNotNull();
    assertThat(eligibility.getDisabilityArms().getIsAdaptedVehicle()).isFalse();
    assertThat(eligibility.getDisabilityArms().getAdaptedVehicleDescription()).isNull();
    assertThat(eligibility.getDisabilityArms().getDrivingFrequency())
        .isEqualTo(JourneyFixture.Values.ARMS_HOW_OFTEN_DRIVE);

    // Arms null for other types...
    EnumSet<EligibilityCodeField> notWalking =
        EnumSet.complementOf(EnumSet.of(ARMS, TERMILL, NONE));
    notWalking.forEach(
        i -> {
          Eligibility eli =
              EligibilityConverter.convert(
                  JourneyFixture.getDefaultJourneyToStep(StepDefinition.DECLARATIONS, i));
          assertThat(eli.getDisabilityArms()).isNull();
        });
  }

  @Test
  public void convert_walkd() {

    Eligibility eligibility =
        EligibilityConverter.convert(
            JourneyFixture.getDefaultJourneyToStep(StepDefinition.DECLARATIONS, WALKD));
    assertThat(eligibility.getWalkingDifficulty()).isNotNull();

    EnumSet<EligibilityCodeField> notWalking =
        EnumSet.complementOf(EnumSet.of(WALKD, TERMILL, NONE));
    notWalking.forEach(
        i -> {
          Journey journey = JourneyFixture.getDefaultJourneyToStep(StepDefinition.DECLARATIONS, i);
          Eligibility eli = EligibilityConverter.convert(journey);
          assertThat(eli.getWalkingDifficulty()).isNull();
        });
  }

  @Test
  public void convert_childbulk() {

    Eligibility eligibility =
        EligibilityConverter.convert(new JourneyBuilder().withEligibility(CHILDBULK).build());
    assertThat(eligibility.getChildUnder3()).isNotNull();

    EnumSet<EligibilityCodeField> notchild =
        EnumSet.complementOf(EnumSet.of(CHILDBULK, TERMILL, NONE));
    notchild.forEach(
        i -> {
          Journey journey = new JourneyBuilder().withEligibility(i).build();
          Eligibility eli = EligibilityConverter.convert(journey);
          assertThat(eli.getChildUnder3()).isNull();
        });
  }

  @Test
  public void convert_healthcare_professionals() {

    EnumSet<EligibilityCodeField> hasHealthcare = EnumSet.of(CHILDBULK, CHILDVEHIC, WALKD);

    hasHealthcare.forEach(
        i -> {
          Eligibility eli =
              EligibilityConverter.convert(new JourneyBuilder().withEligibility(i).build());
          assertThat(eli.getHealthcareProfessionals()).isNotNull();
        });

    EnumSet<EligibilityCodeField> noHealthcare =
        EnumSet.complementOf(EnumSet.of(CHILDBULK, CHILDVEHIC, WALKD, NONE, TERMILL));
    noHealthcare.forEach(
        i -> {
          Journey journey = new JourneyBuilder().withEligibility(i).build();
          Eligibility eli = EligibilityConverter.convert(journey);
          assertThat(eli.getHealthcareProfessionals()).isNull();
        });
  }

  @Test
  public void convert_blind() {

    Eligibility eligibility =
        EligibilityConverter.convert(
            JourneyFixture.getDefaultJourneyToStep(StepDefinition.DECLARATIONS, BLIND));
    assertThat(eligibility.getBlind().getRegisteredAtLaId()).isNotNull();

    EnumSet<EligibilityCodeField> notBlind = EnumSet.complementOf(EnumSet.of(BLIND, TERMILL, NONE));
    notBlind.forEach(
        i -> {
          Journey journey = JourneyFixture.getDefaultJourneyToStep(StepDefinition.DECLARATIONS, i);
          Eligibility eli = EligibilityConverter.convert(journey);
          assertThat(eli.getBlind()).isNull();
        });
  }

  @Test
  public void convert_descriptionofconditions() {
    EnumSet<EligibilityCodeField> hasHealthcare = EnumSet.of(CHILDBULK, CHILDVEHIC, WALKD, ARMS);

    hasHealthcare.forEach(
        i -> {
          Eligibility eli =
              EligibilityConverter.convert(new JourneyBuilder().withEligibility(i).build());
          assertThat(eli.getDescriptionOfConditions()).isNotNull();
        });

    EnumSet<EligibilityCodeField> noHealthcare =
        EnumSet.complementOf(EnumSet.of(CHILDBULK, CHILDVEHIC, WALKD, ARMS, NONE, TERMILL));
    noHealthcare.forEach(
        i -> {
          Journey journey = new JourneyBuilder().withEligibility(i).build();
          Eligibility eli = EligibilityConverter.convert(journey);
          assertThat(eli.getDescriptionOfConditions()).isNull();
        });
  }

  @Test(expected = IllegalStateException.class)
  public void convert_termill_or_none() {
    EnumSet.of(NONE, TERMILL)
        .forEach(
            i -> EligibilityConverter.convert(new JourneyBuilder().withEligibility(i).build()));
  }
}
