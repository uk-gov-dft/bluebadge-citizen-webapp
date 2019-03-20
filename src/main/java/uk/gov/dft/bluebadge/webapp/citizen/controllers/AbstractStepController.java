package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

//@SessionAttributes(Journey.JOURNEY_SESSION_KEY)
public abstract class AbstractStepController implements StepController{
  @Autowired
  protected Journey journey;

  @ModelAttribute(Journey.JOURNEY_SESSION_KEY)
  public Journey getJourney() {
    return this.journey;
  }
}
