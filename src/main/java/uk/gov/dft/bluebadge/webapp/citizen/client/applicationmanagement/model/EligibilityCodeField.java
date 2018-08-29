package uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.validation.constraints.*;

/** Gets or Sets EligibilityCodeField */
public enum EligibilityCodeField {
  PIP("PIP"),

  DLA("DLA"),

  AFRFCS("AFRFCS"),

  WPMS("WPMS"),

  BLIND("BLIND"),

  WALKD("WALKD"),

  ARMS("ARMS"),

  CHILDBULK("CHILDBULK"),

  CHILDVEHIC("CHILDVEHIC");

  private String value;

  EligibilityCodeField(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  // @JsonCreator
  public static EligibilityCodeField fromValue(String text) {
    for (EligibilityCodeField b : EligibilityCodeField.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
