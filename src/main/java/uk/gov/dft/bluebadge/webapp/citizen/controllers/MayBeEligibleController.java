package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MayBeEligibleForm;

@Controller
@RequestMapping(Mappings.URL_MAY_BE_ELIGIBLE)
public class MayBeEligibleController implements StepController {

  private static final String TEMPLATE = "may-be-eligible";

  private final RouteMaster routeMaster;

  @Autowired
  MayBeEligibleController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey, Model model) {

    if (!routeMaster.isValidState(getStepDefinition(), journey)) {
      return routeMaster.backToCompletedPrevious();
    }

    model.addAttribute("isWalkD", EligibilityCodeField.WALKD == journey.getEligibilityCode());
    model.addAttribute("localAuthority", journey.getLocalAuthority());

    return TEMPLATE;
  }

  @GetMapping("/start")
  public String startApplication(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {
    journey.setFormForStep(new MayBeEligibleForm());
    return routeMaster.redirectToOnSuccess(new MayBeEligibleForm());
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.MAY_BE_ELIGIBLE;
  }
}
