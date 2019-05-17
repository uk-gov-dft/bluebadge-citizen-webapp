package uk.gov.dft.bluebadge.webapp.citizen.service;

import lombok.Getter;

@Getter
public class CryptoPostcodeException extends Exception {
  private final String savedPostcode;
  private final String enteredPostcode;

  public CryptoPostcodeException(String savedPostcode, String enteredPostcode) {
    super(
        String.format(
            "Postcodes don't match, saved: %s, entered: %s", savedPostcode, enteredPostcode));
    this.savedPostcode = savedPostcode;
    this.enteredPostcode = enteredPostcode;
  }
}
