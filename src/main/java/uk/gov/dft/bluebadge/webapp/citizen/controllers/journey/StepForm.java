package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

import java.util.Optional;

public interface StepForm {
  StepDefinition getAssociatedStep();

  default Optional<StepDefinition> determineNextStep() {
    return Optional.empty();
  }

  default Optional<StepDefinition> determineNextStep(Journey journey) {
    return Optional.empty();
  }
}
