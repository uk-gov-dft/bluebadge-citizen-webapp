package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Data
@Builder
@EqualsAndHashCode
@Slf4j
public class PaymentUnavailableForm implements StepForm, Serializable {

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.PAYMENT_UNAVAILABLE;
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return true;
  }
}
