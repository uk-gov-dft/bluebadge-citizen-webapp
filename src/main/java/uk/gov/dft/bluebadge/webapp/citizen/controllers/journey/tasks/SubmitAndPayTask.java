package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.tasks;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.BADGE_PAYMENT;

import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Task;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@EqualsAndHashCode(callSuper = true)
public class SubmitAndPayTask extends Task {

  public SubmitAndPayTask(String titleCode, StepDefinition... steps) {
    super(titleCode, steps);
  }

  @Override
  public StepDefinition getFirstStep(Journey journey) {
    if (journey.isPaymentsEnabled()) {
      return BADGE_PAYMENT;
    } else {
      return StepDefinition.SUBMITTED;
    }
  }

  @Override
  public StepDefinition getNextStep(Journey journey, StepDefinition current) {
    if (current == BADGE_PAYMENT) {
      return StepDefinition.SUBMITTED;
    }
    return super.getNextStep(journey, current);
  }
}
