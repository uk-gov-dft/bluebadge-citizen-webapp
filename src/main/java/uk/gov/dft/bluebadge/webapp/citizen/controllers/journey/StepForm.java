package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import java.util.Optional;

public interface StepForm {
  StepDefinition getAssociatedStep();

  default Optional<StepDefinition> determineNextStep() {
    return Optional.empty();
  }
}
