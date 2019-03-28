package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.tasks;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.BADGE_PAYMENT;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.NOT_PAID;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.SUBMITTED;

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
      return SUBMITTED;
    }
  }

  @Override
  public StepDefinition getNextStep(Journey journey, StepDefinition current) {
    switch (current) {
      case BADGE_PAYMENT_RETURN:
        if (!journey.isPaymentSuccessful()) {
          return NOT_PAID;
        } // else fall through to submitted
      case BADGE_PAYMENT:
      case NOT_PAID:
        return SUBMITTED;
      default:
        throw new IllegalStateException(
            "Unsupported step within Submit and Pay task. Step:" + current);
    }
  }
}
