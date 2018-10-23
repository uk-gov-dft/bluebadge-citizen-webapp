package uk.gov.dft.bluebadge.webapp.citizen.appbuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.ARMS;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.CHILDBULK;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.CHILDVEHIC;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.NONE;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.TERMILL;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.WALKD;

import java.util.EnumSet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Eligibility;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

public class EligibilityConverterTest {

  @Mock Journey journey;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void convert_walkd() {

    JourneyFixture.configureMockJourney(journey, WALKD);
    Eligibility eligibility = EligibilityConverter.convert(journey);
    assertThat(eligibility.getWalkingDifficulty()).isNotNull();

    EnumSet<EligibilityCodeField> notWalking =
        EnumSet.complementOf(EnumSet.of(WALKD, TERMILL, NONE));
    notWalking.forEach(
        i -> {
          JourneyFixture.configureMockJourney(journey, i);
          Eligibility eli = EligibilityConverter.convert(journey);
          assertThat(eli.getWalkingDifficulty()).isNull();
        });
  }

  @Test
  public void convert_childbulk() {
    JourneyFixture.configureMockJourney(journey, CHILDBULK);
    Eligibility eligibility = EligibilityConverter.convert(journey);
    assertThat(eligibility.getChildUnder3()).isNotNull();

    EnumSet<EligibilityCodeField> notchild =
        EnumSet.complementOf(EnumSet.of(CHILDBULK, TERMILL, NONE));
    notchild.forEach(
        i -> {
          JourneyFixture.configureMockJourney(journey, i);
          Eligibility eli = EligibilityConverter.convert(journey);
          assertThat(eli.getChildUnder3()).isNull();
        });
  }

  @Test
  public void convert_healthcare_professionals() {

    EnumSet<EligibilityCodeField> hasHealthcare = EnumSet.of(CHILDBULK, CHILDVEHIC, WALKD);

    hasHealthcare.forEach(
        i -> {
          JourneyFixture.configureMockJourney(journey, i);
          Eligibility eli = EligibilityConverter.convert(journey);
          assertThat(eli.getHealthcareProfessionals()).isNotNull();
        });

    EnumSet<EligibilityCodeField> noHealthcare =
        EnumSet.complementOf(EnumSet.of(CHILDBULK, CHILDVEHIC, WALKD, NONE, TERMILL));
    noHealthcare.forEach(
        i -> {
          JourneyFixture.configureMockJourney(journey, i);
          Eligibility eli = EligibilityConverter.convert(journey);
          assertThat(eli.getHealthcareProfessionals()).isNull();
        });
  }

  @Test
  public void convert_descriptionofconditions() {
    EnumSet<EligibilityCodeField> hasHealthcare = EnumSet.of(CHILDBULK, CHILDVEHIC, WALKD, ARMS);

    hasHealthcare.forEach(
        i -> {
          JourneyFixture.configureMockJourney(journey, i);
          Eligibility eli = EligibilityConverter.convert(journey);
          assertThat(eli.getDescriptionOfConditions()).isNotNull();
        });

    EnumSet<EligibilityCodeField> noHealthcare =
        EnumSet.complementOf(EnumSet.of(CHILDBULK, CHILDVEHIC, WALKD, ARMS, NONE, TERMILL));
    noHealthcare.forEach(
        i -> {
          JourneyFixture.configureMockJourney(journey, i);
          Eligibility eli = EligibilityConverter.convert(journey);
          assertThat(eli.getDescriptionOfConditions()).isNull();
        });
  }

  @Test(expected = IllegalStateException.class)
  public void convert_termill_or_none() {
    EnumSet.of(NONE, TERMILL)
        .forEach(
            i -> {
              JourneyFixture.configureMockJourney(journey, i);
              EligibilityConverter.convert(journey);
            });
  }

  @Test
  public void getHealthcareProfessionals() {}
}
