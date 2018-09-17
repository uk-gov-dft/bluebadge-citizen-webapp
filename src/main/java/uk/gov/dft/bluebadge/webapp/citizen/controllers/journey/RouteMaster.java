package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.StepController;

@Component
public class RouteMaster {

  public String redirectToOnSuccess(StepController currentStep) {
    StepDefinition nextStep =
        currentStep
            .getStepDefinition()
            .getNext()
            .stream()
            .findFirst()
            .orElseThrow(
                () ->
                    new IllegalStateException("Failed to determine next step for: " + currentStep));

    return "redirect:" + Mappings.getUrl(nextStep);
  }
}
