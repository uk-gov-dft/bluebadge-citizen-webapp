package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings.URL_ROOT;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinitionEnum;

@Controller
public class HomeController implements StepController {

  @GetMapping(URL_ROOT)
  public String show() {
    return RouteMaster.redirectToOnSuccess(this);
  }

  @Override
  public StepDefinitionEnum getStepDefinition() {
    return StepDefinitionEnum.HOME;
  }
}
