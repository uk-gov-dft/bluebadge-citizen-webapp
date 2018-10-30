package uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Contact {

  private String fullName;
  private String buildingStreet;
  private String line2;
  private String townCity;
  private String postCode;
  private String primaryPhoneNumber;
  private String secondaryPhoneNumber;
  private String emailAddress;
}
