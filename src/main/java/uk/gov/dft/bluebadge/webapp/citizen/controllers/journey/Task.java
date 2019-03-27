package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import java.util.List;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

public interface Task {
  String getTitleCode();

  List<StepDefinition> getSteps();

  default StepDefinition getNextStep(Journey journey, StepDefinition current) {
    try {
      List<StepDefinition> steps = getSteps();
      return steps.get(steps.indexOf(current) + 1);
    } catch (IndexOutOfBoundsException e) {
      // End of steps for this task
      return null;
    }
  }

  default StepDefinition getFirstStep(Journey journey) {
    return getSteps().get(0);
  }
}
