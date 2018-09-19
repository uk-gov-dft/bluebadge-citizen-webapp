package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import org.springframework.stereotype.Component;

@Component
public class RouteMaster {

  public String redirectToOnSuccess(StepForm form) {
    StepDefinition currentStep = form.getAssociatedStep();

    StepDefinition nextStep =
        form.determineNextStep().orElseGet(() -> currentStep.getDefaultNext());

    if (!currentStep.getNext().contains(nextStep)) {
      throw new IllegalStateException(
          "Next step '" + nextStep + "', not associated with the current one:" + currentStep);
    }

    return "redirect:" + Mappings.getUrl(nextStep);
  }

  public String redirectToOnSuccess(StepDefinition currentStep) {
    StepDefinition nextStep = currentStep.getDefaultNext();
    return "redirect:" + Mappings.getUrl(nextStep);
  }

  public String startingPoint() {
    return "redirect:" + Mappings.getUrl(StepDefinition.HOME.getDefaultNext());
  }

  public String backToCompletedPrevious() {
    return "redirect:" + Mappings.getUrl(StepDefinition.HOME);
  }
}
