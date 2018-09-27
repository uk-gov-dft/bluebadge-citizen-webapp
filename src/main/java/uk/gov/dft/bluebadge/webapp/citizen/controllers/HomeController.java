package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings.URL_ROOT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;

@Controller
public class HomeController implements StepController {
  private final RouteMaster routeMaster;

  @Autowired
  public HomeController(RouteMaster routeMaster) {
    this.routeMaster = routeMaster;
  }

  @GetMapping(URL_ROOT)
  public String show() {
    return routeMaster.startingPoint();
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.HOME;
  }
}
