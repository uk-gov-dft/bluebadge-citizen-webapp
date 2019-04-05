package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.tasks;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.APPLICANT_TYPE;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.ELIGIBLE;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.MAY_BE_ELIGIBLE;

import java.util.EnumSet;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Task;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@EqualsAndHashCode(callSuper = true)
public class CheckEligibilityTask extends Task {
  private static final EnumSet<StepDefinition> END_OF_TASK_STEPS =
      EnumSet.of(ELIGIBLE, MAY_BE_ELIGIBLE);

  public CheckEligibilityTask(String titleCode, StepDefinition... steps) {
    super(titleCode, steps);
  }

  @Override
  public StepDefinition getFirstStep(Journey journey) {
    return APPLICANT_TYPE;
  }

  @Override
  public StepDefinition getNextStep(Journey journey, StepDefinition current) {
    if (END_OF_TASK_STEPS.contains(current)) {
      return null;
    }

    // TODO Terminatorary steps. (signpost, org etc...)

    return super.getNextStep(journey, current);
  }
}
