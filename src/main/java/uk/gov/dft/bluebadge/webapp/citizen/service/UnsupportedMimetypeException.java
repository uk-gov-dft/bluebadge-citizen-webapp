package uk.gov.dft.bluebadge.webapp.citizen.service;

public class UnsupportedMimetypeException extends RuntimeException {
  public UnsupportedMimetypeException(String mimetype) {
    super("Unsupported mimetype:" + mimetype);
  }
}
