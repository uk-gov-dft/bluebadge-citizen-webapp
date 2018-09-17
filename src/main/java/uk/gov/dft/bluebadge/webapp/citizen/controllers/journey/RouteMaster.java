package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import org.springframework.stereotype.Component;

import uk.gov.dft.bluebadge.webapp.citizen.controllers.StepController;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

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

  public String backToCompletedPrevious() {
    return "redirect:" + Mappings.getUrl(StepDefinition.HOME);
  }

  public boolean isValidState(StepController stepController, Journey journey) {
    StepDefinition stepDefinition = stepController.getStepDefinition();
    switch (stepDefinition) {
      case HEALTH_CONDITIONS:
        if (null == journey.getApplicantForm()) {
          return false;
        }
    }
    return true;
  }
}
