package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import java.util.List;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

public interface TaskConfig {
  default StepDefinition getNextStep(
      Journey journey, List<StepDefinition> steps, StepDefinition current) {
    try {
      return steps.get(steps.indexOf(current) + 1);
    } catch (IndexOutOfBoundsException e) {
      // End of steps for this task
      return null;
    }
  }

  default StepDefinition getFirstStep(Journey journey, List<StepDefinition> steps) {
    return steps.get(0);
  }
}
