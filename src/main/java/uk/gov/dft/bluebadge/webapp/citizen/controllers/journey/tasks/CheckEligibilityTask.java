package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.tasks;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.ELIGIBLE;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.MAY_BE_ELIGIBLE;

import com.google.common.collect.ImmutableList;
import java.util.EnumSet;
import java.util.List;
import lombok.Getter;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Task;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

public class CheckEligibilityTask implements Task {
  private static final EnumSet<StepDefinition> END_OF_TASK_STEPS =
      EnumSet.of(ELIGIBLE, MAY_BE_ELIGIBLE);

  @Getter private final String titleCode;
  @Getter private final List<StepDefinition> steps;

  public CheckEligibilityTask(String titleCode, StepDefinition... steps) {
    this.titleCode = titleCode;
    this.steps = ImmutableList.copyOf(steps);
  }

  @Override
  public StepDefinition getNextStep(Journey journey, StepDefinition current) {
    if (END_OF_TASK_STEPS.contains(current)) {
      return null;
    }

    // TODO Terminatorary steps. (signpost, org etc...)

    try {
      return steps.get(steps.indexOf(current) + 1);
    } catch (IndexOutOfBoundsException e) {
      // End of steps for this task
      return null;
    }
  }
}
