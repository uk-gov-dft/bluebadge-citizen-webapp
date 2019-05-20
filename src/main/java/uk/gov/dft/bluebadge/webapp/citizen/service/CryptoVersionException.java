package uk.gov.dft.bluebadge.webapp.citizen.service;

import lombok.Getter;

@Getter
public class CryptoVersionException extends Exception {
  private final String encryptedVersion;
  private final String currentVersion;

  public CryptoVersionException(String encryptedVersion, String currentVersion) {
    super(
        String.format(
            "Journey application versions don't match, saved: %s, running app: %s",
            encryptedVersion, currentVersion));
    this.encryptedVersion = encryptedVersion;
    this.currentVersion = currentVersion;
  }
}
