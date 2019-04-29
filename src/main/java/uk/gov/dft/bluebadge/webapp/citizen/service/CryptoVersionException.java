package uk.gov.dft.bluebadge.webapp.citizen.service;

import lombok.Data;

@Data
public class CryptoVersionException extends Exception {
  private String encryptedVersion;

  public CryptoVersionException(String encryptedVersion) {
    super();
    this.encryptedVersion = encryptedVersion;
  }
}
