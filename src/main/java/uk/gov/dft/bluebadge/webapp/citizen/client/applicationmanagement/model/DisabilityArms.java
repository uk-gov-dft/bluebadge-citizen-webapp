package uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DisabilityArms {

  private final String drivingFrequency;
  private final Boolean isAdaptedVehicle;
  private final String adaptedVehicleDescription;

  DisabilityArms(
      String drivingFrequency, Boolean isAdaptedVehicle, String adaptedVehicleDescription) {
    this.drivingFrequency = drivingFrequency;
    this.isAdaptedVehicle = isAdaptedVehicle;
    if (isAdaptedVehicle) {
      this.adaptedVehicleDescription = adaptedVehicleDescription;
    } else {
      this.adaptedVehicleDescription = null;
    }
  }
}
