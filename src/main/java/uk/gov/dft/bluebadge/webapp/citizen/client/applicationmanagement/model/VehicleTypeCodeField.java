package uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.validation.constraints.*;

/** Gets or Sets VehicleTypeCodeField */
public enum VehicleTypeCodeField {
  CAR("CAR"),

  PEOPLECAR("PEOPLECAR"),

  MINIBUS("MINIBUS"),

  OTHERVEH("OTHERVEH");

  private String value;

  VehicleTypeCodeField(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  // @JsonCreator
  public static VehicleTypeCodeField fromValue(String text) {
    for (VehicleTypeCodeField b : VehicleTypeCodeField.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
