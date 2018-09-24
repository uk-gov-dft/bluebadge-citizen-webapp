package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.service.referencedata.ReferenceDataService;

@Controller
@RequestMapping(Mappings.URL_MAY_BE_ELIGIBLE)
public class MayBeEligibleController implements StepController {

  private static final String TEMPLATE = "may-be-eligible";

  private final RouteMaster routeMaster;
  private final ReferenceDataService referenceDataService;

  @Autowired
  public MayBeEligibleController(
      RouteMaster routeMaster, ReferenceDataService referenceDataService) {
    this.routeMaster = routeMaster;
    this.referenceDataService = referenceDataService;
  }

  @GetMapping
  public String show(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey, Model model) {

    if (!journey.isValidState(getStepDefinition())) {
      return routeMaster.backToCompletedPrevious();
    }

    String laShortCode = journey.getYourIssuingAuthorityForm().getLocalAuthorityShortCode();
    model.addAttribute("localAuthority", referenceDataService.retrieveLocalAuthority(laShortCode));

    return TEMPLATE;
  }

  @GetMapping("/start")
  public String startApplication(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {
    return routeMaster.redirectToOnSuccess(StepDefinition.MAY_BE_ELIGIBLE);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.MAY_BE_ELIGIBLE;
  }
}
