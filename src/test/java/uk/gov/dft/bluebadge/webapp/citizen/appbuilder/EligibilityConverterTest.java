package uk.gov.dft.bluebadge.webapp.citizen.appbuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.ARMS;
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

public class EligibilityConverterTest {

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
          Eligibility eli =
              EligibilityConverter.convert(
                  JourneyFixture.getDefaultJourneyToStep(StepDefinition.DECLARATIONS, i));
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
          Eligibility eli =
              EligibilityConverter.convert(new JourneyBuilder().withEligibility(i).build());
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
          Eligibility eli =
              EligibilityConverter.convert(new JourneyBuilder().withEligibility(i).build());
          assertThat(eli.getHealthcareProfessionals()).isNull();
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
          Eligibility eli =
              EligibilityConverter.convert(new JourneyBuilder().withEligibility(i).build());
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
