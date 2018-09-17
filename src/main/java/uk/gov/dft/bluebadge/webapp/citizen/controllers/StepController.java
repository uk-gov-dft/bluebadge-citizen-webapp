package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@SessionAttributes(Journey.JOURNEY_SESSION_KEY)
public interface StepController {

  StepDefinition getStepDefinition();

  /**
   * Binds journey domain object into the session
   *
   * @return
   */
  @ModelAttribute(Journey.JOURNEY_SESSION_KEY)
  default Journey getJourney() {
    return new Journey();
  }
}
