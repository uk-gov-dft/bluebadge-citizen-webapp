package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.support.SessionStatus;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

public abstract class BaseStepController implements StepController {
  protected final RouteMaster routeMaster;

  public BaseStepController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  public String show(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      Model model,
      SessionStatus sessionStatus) {
    if (!journey.isValidState(getStepDefinition())) {
      return routeMaster.backToCompletedPrevious();
    }

    model.addAttribute("localAuthority", journey.getLocalAuthority());

    sessionStatus.setComplete();
    return getTemplate();
  }

  protected abstract String getTemplate();
}
