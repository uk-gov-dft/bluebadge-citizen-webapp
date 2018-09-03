package uk.gov.dft.bluebadge.webapp.citizen.client.common;

public class ServiceException extends RuntimeException {
  public ServiceException(String s, Exception e) {
    super(s, e);
  }

  public ServiceException(String message) {
    super(message);
  }
}
