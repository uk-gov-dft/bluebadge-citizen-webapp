package uk.gov.dft.bluebadge.webapp.citizen.service;

import lombok.Getter;

@Getter
public class CryptoVersionException extends Exception {
  private final String encryptedVersion;
  private final String currentVersion;

  public CryptoVersionException(String message, String encryptedVersion, String currentVersion) {
    super(message);
    this.encryptedVersion = encryptedVersion;
    this.currentVersion = currentVersion;
  }
}
