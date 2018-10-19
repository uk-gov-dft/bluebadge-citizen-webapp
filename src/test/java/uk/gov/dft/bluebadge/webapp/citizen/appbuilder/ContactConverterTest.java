package uk.gov.dft.bluebadge.webapp.citizen.appbuilder;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Contact;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

public class ContactConverterTest {

  @Mock Journey journey;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    JourneyFixture.configureMockJourney(journey);
  }

  @Test
  public void convert() {
    Contact contact = ContactConverter.convert(journey);
    assertThat(contact.getBuildingStreet()).isEqualTo(JourneyFixture.Values.ADDRESS_LINE_1);
    assertThat(contact.getLine2()).isEqualTo(JourneyFixture.Values.ADDRESS_LINE_2);
    assertThat(contact.getPostCode()).isEqualTo(JourneyFixture.Values.POSTCODE);
    assertThat(contact.getTownCity()).isEqualTo(JourneyFixture.Values.TOWN_OR_CITY);
    assertThat(contact.getFullName()).isEqualTo(JourneyFixture.Values.CONTACT_NAME);
    assertThat(contact.getPrimaryPhoneNumber()).isEqualTo(JourneyFixture.Values.PRIMARY_PHONE_NO);
    assertThat(contact.getSecondaryPhoneNumber())
        .isEqualTo(JourneyFixture.Values.SECONDARY_PHONE_NO);
    assertThat(contact.getEmailAddress()).isEqualTo(JourneyFixture.Values.EMAIL_ADDRESS);
  }
}
