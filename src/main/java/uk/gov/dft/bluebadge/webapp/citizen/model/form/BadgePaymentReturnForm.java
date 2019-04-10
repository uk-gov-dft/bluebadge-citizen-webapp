package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Slf4j
@Data
@Builder
@EqualsAndHashCode
public class BadgePaymentReturnForm implements StepForm, Serializable {

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.BADGE_PAYMENT_RETURN;
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return true;
  }
}
