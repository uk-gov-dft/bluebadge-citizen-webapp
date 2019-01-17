package uk.gov.dft.bluebadge.webapp.citizen.controllers.mainreason;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.BaseFinalStepController;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.StepController;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Controller
@RequestMapping(Mappings.URL_NOT_ELIGIBLE)
@SuppressWarnings({"common-java:DuplicatedBlocksSource"})
public class NotEligibleController extends BaseFinalStepController implements StepController {
  private static final String TEMPLATE = "mainreason/not-eligible";

  @Autowired
  NotEligibleController(RouteMaster routeMaster) {
    super(routeMaster);
  }

  @Override
  protected String getTemplate() {
    return TEMPLATE;
  }

  @GetMapping
  public String show(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      Model model,
      SessionStatus sessionStatus) {

    return super.show(journey, model, sessionStatus);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.NOT_ELIGIBLE;
  }
}
