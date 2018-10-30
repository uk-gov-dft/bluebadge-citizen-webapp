package uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Vehicle {

  private String registrationNumber;
  private VehicleTypeCodeField typeCode;
  private String usageFrequency;
}
