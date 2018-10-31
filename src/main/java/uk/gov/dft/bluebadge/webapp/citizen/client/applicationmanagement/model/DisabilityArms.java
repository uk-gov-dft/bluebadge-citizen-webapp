package uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DisabilityArms {

  private String drivingFrequency;
  private Boolean isAdaptedVehicle;
  private String adaptedVehicleDescription;
}
