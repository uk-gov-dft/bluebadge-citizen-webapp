package uk.gov.dft.bluebadge.webapp.citizen.model;

import lombok.Data;

@Data
public class RadioOption {

  private String shortCode;
  private String messageKey;

  public RadioOption(String shortCode, String messageKey) {
    this.shortCode = shortCode;
    this.messageKey = messageKey;
  }
}
