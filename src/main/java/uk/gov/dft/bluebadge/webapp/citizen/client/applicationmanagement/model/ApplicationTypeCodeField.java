package uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.validation.constraints.*;

/** Gets or Sets ApplicationTypeCodeField */
public enum ApplicationTypeCodeField {
  NEW("NEW"),

  RENEW("RENEW"),

  CANCEL("CANCEL"),

  REPLACE("REPLACE");

  private String value;

  ApplicationTypeCodeField(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  //  @JsonCreator
  public static ApplicationTypeCodeField fromValue(String text) {
    for (ApplicationTypeCodeField b : ApplicationTypeCodeField.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
