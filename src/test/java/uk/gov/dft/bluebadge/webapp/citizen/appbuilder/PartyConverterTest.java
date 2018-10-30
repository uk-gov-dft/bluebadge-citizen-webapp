package uk.gov.dft.bluebadge.webapp.citizen.appbuilder;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Party;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.PartyTypeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyBuilder;

public class PartyConverterTest {

  @Test
  public void convert() {
    Party result = PartyConverter.convert(new JourneyBuilder().forYou().build());

    assertThat(result.getTypeCode()).isEqualTo(PartyTypeCodeField.PERSON);
    assertThat(result.getContact()).isNotNull();
    assertThat(result.getPerson()).isNotNull();
    assertThat(result.getOrganisation()).isNull();
  }
}
