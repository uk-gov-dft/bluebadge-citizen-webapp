package uk.gov.dft.bluebadge.webapp.citizen.controllers.organisation;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.support.SessionStatus;

import uk.gov.dft.bluebadge.webapp.citizen.controllers.StepController;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

public class OrganisationNotEligibleController implements StepController {
	  private static final String TEMPLATE = "organisation-not-eligible";

	  private final RouteMaster routeMaster;

	  @Autowired
	  public OrganisationNotEligibleController(RouteMaster routeMaster) {
	    this.routeMaster = routeMaster;
	  }

	  @GetMapping
	  public String show(
	      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
	      Model model,
	      SessionStatus sessionStatus) {
	    if (!journey.isValidState(getStepDefinition())) {
	      return routeMaster.backToCompletedPrevious();
	    }

	    model.addAttribute("localAuthority", journey.getLocalAuthority());

	    sessionStatus.setComplete();
	    return TEMPLATE;
	  }

	  @Override
	  public StepDefinition getStepDefinition() {
	    return StepDefinition.ORGANISATION_NOT_ELIGIBLE;
	  }

}
