package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.tasks;

public class InvalidStateForJourneyException extends RuntimeException {
  public InvalidStateForJourneyException(String msg) {
    super(msg);
  }
}
