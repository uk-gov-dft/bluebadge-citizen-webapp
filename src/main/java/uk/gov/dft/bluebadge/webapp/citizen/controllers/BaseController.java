package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@SessionAttributes(Journey.JOURNEY_SESSION_KEY)
public abstract class BaseController {

  /**
   * Binds journey domain object into the session
   *
   * @return
   */
  @ModelAttribute(Journey.JOURNEY_SESSION_KEY)
  public Journey getJourney() {
    return new Journey();
  }
}
