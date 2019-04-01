package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings.URL_SUBMIT_APPLICATION;
import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.dft.bluebadge.webapp.citizen.appbuilder.JourneyToApplicationConverter;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.service.ApplicationManagementService;

@Controller
@RequestMapping(URL_SUBMIT_APPLICATION)
public class SubmitApplicationController implements StepController {

  private static final String TEMPLATE = "application-end/submit-application";
  private final RouteMaster routeMaster;
  private final ApplicationManagementService appService;

  @Autowired
  SubmitApplicationController(RouteMaster routeMaster, ApplicationManagementService appService) {
    this.routeMaster = routeMaster;
    this.appService = appService;
  }

  @GetMapping
  public String show(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {
    if(!routeMaster.isValidState(getStepDefinition(), journey)){
      return routeMaster.backToCompletedPrevious(journey);
    }

    return TEMPLATE;
  }

  @PostMapping
  public String submit(@ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {
    if(!routeMaster.isValidState(getStepDefinition(), journey)){
      throw new IllegalStateException("Not in a valid state to submit.");
    }

    appService.create(JourneyToApplicationConverter.convert(journey));

    return routeMaster.redirectToOnSuccess(getStepDefinition(), journey);
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.SUBMIT_APPLICATION;
  }
}
