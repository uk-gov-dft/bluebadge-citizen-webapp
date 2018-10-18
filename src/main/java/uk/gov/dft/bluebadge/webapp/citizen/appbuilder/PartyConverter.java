package uk.gov.dft.bluebadge.webapp.citizen.appbuilder;

import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Party;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.PartyTypeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

public class PartyConverter {

  public static Party convert(Journey journey) {
    return Party.builder()
        .typeCode(PartyTypeCodeField.PERSON)
        .contact(ContactConverter.convert(journey))
        .person(PersonConverter.convert(journey))
        .build();
  }
}
