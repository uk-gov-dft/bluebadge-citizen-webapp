package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings.URL_APPLICATION_SUBMITTED;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.support.SessionStatus;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Controller
public class SubmittedController implements StepController {

  private static final String TEMPLATE_APPLICATION_SUBMITTED = "application-end/submitted";

  private final RouteMaster routeMaster;

  @Autowired
  public SubmittedController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @GetMapping(URL_APPLICATION_SUBMITTED)
  public String showSubmitted(
      SessionStatus sessionStatus,
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      Model model) {
    if (!journey.isValidState(getStepDefinition())) {
      return routeMaster.backToCompletedPrevious();
    }

    model.addAttribute("mainMessage", journey.who + "submittedPage.content.p1");
    sessionStatus.setComplete();
    return TEMPLATE_APPLICATION_SUBMITTED;
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.SUBMITTED;
  }
}
