package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import java.util.List;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

public class SubmitAndPayTask implements TaskConfig {
  @Override
  public StepDefinition getFirstStep(Journey journey, List<StepDefinition> steps) {
    if (journey.isPaymentsEnabled()) {
      return StepDefinition.BADGE_PAYMENT;
    } else {
      return StepDefinition.SUBMITTED;
    }
  }
}
