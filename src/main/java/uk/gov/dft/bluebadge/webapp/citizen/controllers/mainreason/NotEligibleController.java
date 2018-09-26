package uk.gov.dft.bluebadge.webapp.citizen.controllers.mainreason;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.StepController;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.service.referencedata.ReferenceDataService;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

@Controller
@RequestMapping(Mappings.URL_NOT_ELIGIBLE)
public class NotEligibleController implements StepController {
  private static final String TEMPLATE = "mainreason/not-eligible";

  private final RouteMaster routeMaster;

  @Autowired
  public NotEligibleController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String show(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey, Model model) {
    if (!journey.isValidState(getStepDefinition())) {
      return routeMaster.backToCompletedPrevious();
    }

    String laShortCode = journey.getYourIssuingAuthorityForm().getLocalAuthorityShortCode();
    model.addAttribute("localAuthority", journey.getLocalAuthority());

    return TEMPLATE;
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.NOT_ELIGIBLE;
  }
}
