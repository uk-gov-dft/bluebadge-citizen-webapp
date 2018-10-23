package uk.gov.dft.bluebadge.webapp.citizen.appbuilder;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Party;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.PartyTypeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

public class PartyConverterTest {

  @Mock Journey journey;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    JourneyFixture.configureMockJourney(journey);
  }

  @Test
  public void convert() {
    Party result = PartyConverter.convert(journey);

    assertThat(result.getTypeCode()).isEqualTo(PartyTypeCodeField.PERSON);
    assertThat(result.getContact()).isNotNull();
    assertThat(result.getPerson()).isNotNull();
    assertThat(result.getOrganisation()).isNull();
  }
}
