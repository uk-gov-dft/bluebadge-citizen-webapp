package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Data
@Builder
@EqualsAndHashCode
public class PayForTheBadgeRetryForm implements StepForm, Serializable {

  private String retry;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.PAY_FOR_THE_BADGE_RETRY;
  }

  @Override
  public Optional<StepDefinition> determineNextStep(Journey journey) {
    // This is not the only posible transition. In the controller we redirect directly to a URL
    // prior to this logic in certain circumstances.
    return Optional.of(StepDefinition.SUBMITTED);
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return true;
  }
}
