package uk.gov.dft.bluebadge.webapp.citizen.service;

import lombok.Getter;

@Getter
public class CryptoPostcodeException extends Exception {
  private final String savedPostcode;
  private final String enteredPostcode;

  public CryptoPostcodeException(String message, String savedPostcode, String enteredPostcode) {
    super(message);
    this.savedPostcode = savedPostcode;
    this.enteredPostcode = enteredPostcode;
  }
}
