package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import java.util.Optional;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

public interface StepForm {
  StepDefinition getAssociatedStep();

  default Optional<StepDefinition> determineNextStep() {
    return Optional.empty();
  }

  default Optional<StepDefinition> determineNextStep(Journey journey) {
    return Optional.empty();
  }
}
