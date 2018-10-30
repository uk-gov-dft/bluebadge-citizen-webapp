package uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Party {
  private PartyTypeCodeField typeCode;
  private Contact contact;
  private Person person;
  private Organisation organisation;
}
