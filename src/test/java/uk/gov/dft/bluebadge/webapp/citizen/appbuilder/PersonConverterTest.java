package uk.gov.dft.bluebadge.webapp.citizen.appbuilder;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Person;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class PersonConverterTest {

  @Mock Journey journey;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    JourneyFixture.configureMockJourney(journey);
  }

  @Test
  public void convert() {
    Person result = PersonConverter.convert(journey);
    assertThat(result.getBadgeHolderName()).isEqualTo(JourneyFixture.Values.FULL_NAME);
    assertThat(result.getNameAtBirth()).isEqualTo(JourneyFixture.Values.BIRTH_NAME);
    assertThat(result.getGenderCode()).isEqualTo(JourneyFixture.Values.GENDER);
    assertThat(result.getDob()).isEqualTo(JourneyFixture.Values.DOB_AS_EQUAL_TO_STRING);
    assertThat(result.getNino()).isEqualTo(JourneyFixture.Values.NINO);

    // Try with null nino.
    when(journey.getFormForStep(StepDefinition.NINO)).thenReturn(null);
    result = PersonConverter.convert(journey);
    assertThat(result.getNino()).isNull();
  }
}
