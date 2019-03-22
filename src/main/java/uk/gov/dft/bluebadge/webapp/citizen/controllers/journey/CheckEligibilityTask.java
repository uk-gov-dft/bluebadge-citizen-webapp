package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.ELIGIBLE;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.MAY_BE_ELIGIBLE;

import java.util.EnumSet;
import java.util.List;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

public class CheckEligibilityTask implements TaskConfig {
  private static final EnumSet<StepDefinition> END_OF_TASK_STEPS =
      EnumSet.of(ELIGIBLE, MAY_BE_ELIGIBLE);

  @Override
  public StepDefinition getNextStep(
      Journey journey, List<StepDefinition> steps, StepDefinition current) {
    if (END_OF_TASK_STEPS.contains(current)) {
      return null;
    }
    try {
      return steps.get(steps.indexOf(current) + 1);
    } catch (IndexOutOfBoundsException e) {
      // End of steps for this task
      return null;
    }
  }
}
