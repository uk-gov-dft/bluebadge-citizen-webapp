package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Data
@Builder
@EqualsAndHashCode
public class BadgePaymentRetryForm implements StepForm, Serializable {

  private String retry;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.BADGE_PAYMENT_RETRY;
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return true;
  }
}
