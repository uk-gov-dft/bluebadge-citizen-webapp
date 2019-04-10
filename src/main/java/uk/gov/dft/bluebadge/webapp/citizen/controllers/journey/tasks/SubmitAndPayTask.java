package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.tasks;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.BADGE_PAYMENT;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.NOT_PAID;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.SUBMITTED;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.SUBMIT_APPLICATION;

import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Task;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@EqualsAndHashCode(callSuper = true)
public class SubmitAndPayTask extends Task {

  private final String submitAndPayTitle;
  private final String submitTitle;

  public SubmitAndPayTask(String submitAndPayTitle, String submitTitle, StepDefinition... steps) {
    super(null, steps);
    this.submitAndPayTitle = submitAndPayTitle;
    this.submitTitle = submitTitle;
  }

  @Override
  public String getTitleCode(Journey journey) {
    return journey.isPaymentsEnabled() ? submitAndPayTitle : submitTitle;
  }

  @Override
  public StepDefinition getFirstStep(Journey journey) {
    if (journey.isPaymentsEnabled()) {
      return BADGE_PAYMENT;
    } else {
      return SUBMIT_APPLICATION;
    }
  }

  @Override
  public StepDefinition getNextStep(Journey journey, StepDefinition current) {
    switch (current) {
      case BADGE_PAYMENT_RETURN:
        if (!journey.isPaymentSuccessful() && !journey.isPaymentStatusUnknown()) {
          return NOT_PAID;
        } // else fall through to submitted
      case BADGE_PAYMENT:
      case NOT_PAID:
      case SUBMIT_APPLICATION:
        return SUBMITTED;
      default:
        throw new IllegalStateException(
            "Unsupported step within Submit and Pay task. Step:" + current);
    }
  }

  /** This task is only enabled once all previous have been completed */
  @Override
  public boolean isValidStep(Journey journey, StepDefinition step) {
    if (journey.isPaymentsEnabled()) {
      if (step == BADGE_PAYMENT) {
        return true;
      }

      return null != journey.getFormForStep(BADGE_PAYMENT);
    }
    return true;
  }
}
