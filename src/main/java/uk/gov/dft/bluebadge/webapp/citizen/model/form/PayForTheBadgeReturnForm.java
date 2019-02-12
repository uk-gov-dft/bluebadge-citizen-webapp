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
public class PayForTheBadgeReturnForm implements StepForm, Serializable {

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.PAY_FOR_THE_BADGE_RETURN;
  }

  @Override
  public Optional<StepDefinition> determineNextStep(Journey journey) {
    if (journey.isPaymentSuccessful()) {
      return Optional.of(StepDefinition.SUBMITTED);
    } else {
      log.error(
          "Payment failed. Status code [], reference [], journey uuid []",
          journey.getPaymentStatus(),
          journey.getPaymentReference(),
          journey.getPaymentJourneyUuid());
      return Optional.of(StepDefinition.PAY_FOR_THE_BADGE);
    }
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return true;
  }
}
