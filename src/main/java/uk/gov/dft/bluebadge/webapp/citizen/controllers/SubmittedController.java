package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinitionEnum;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings.URL_APPLICATION_SUBMITTED;

@Controller
public class SubmittedController  implements StepController {

  private static final String TEMPLATE_APPLICATION_SUBMITTED = "application-end/submitted";

  @GetMapping(URL_APPLICATION_SUBMITTED)
  public String showSubmitted() {
    return TEMPLATE_APPLICATION_SUBMITTED;
  }

  @Override
  public StepDefinitionEnum getStepDefinition() {
    return StepDefinitionEnum.SUBMITTED;
  }
}
