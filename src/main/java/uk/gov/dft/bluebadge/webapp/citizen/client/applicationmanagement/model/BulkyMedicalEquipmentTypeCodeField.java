package uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model;

import com.fasterxml.jackson.annotation.JsonValue;

/** Gets or Sets BulkyMedicalEquipmentTypeCodeField */
public enum BulkyMedicalEquipmentTypeCodeField {
  OTHER("OTHER"),

  CAST("CAST"),

  OXYSAT("OXYSAT"),

  OXYADMIN("OXYADMIN"),

  SYRINGE("SYRINGE"),

  PARENT("PARENT"),

  PUMP("PUMP"),

  SUCTION("SUCTION"),

  VENT("VENT");

  private String value;

  BulkyMedicalEquipmentTypeCodeField(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  // @JsonCreator
  public static BulkyMedicalEquipmentTypeCodeField fromValue(String text) {
    for (BulkyMedicalEquipmentTypeCodeField b : BulkyMedicalEquipmentTypeCodeField.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
