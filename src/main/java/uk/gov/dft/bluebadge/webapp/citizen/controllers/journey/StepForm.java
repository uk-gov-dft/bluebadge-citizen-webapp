package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import java.util.Collections;
import java.util.List;
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

  /**
   * Journey is included to provide context so cleaning forms can be dynamic.
   *
   * @param journey
   * @return
   */
  default List<StepDefinition> getCleanUpSteps(Journey journey) {
    return Collections.emptyList();
  }
}
