package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import java.util.Optional;
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
public class PayForTheBlueBadgeReturnForm implements StepForm, Serializable {

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.PAY_FOR_THE_BLUE_BADGE_RETURN;
  }

  @Override
  public Optional<StepDefinition> determineNextStep(Journey journey) {
    if (journey.getPaymentStatusResponse().getStatus().equals("created")) {
      return Optional.of(StepDefinition.SUBMITTED);
    } else {
      // TODO: Complete the message
      log.error("There was an error in the payment");
      return Optional.of(StepDefinition.PAY_FOR_THE_BLUE_BADGE);
    }
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return true;
  }
}
