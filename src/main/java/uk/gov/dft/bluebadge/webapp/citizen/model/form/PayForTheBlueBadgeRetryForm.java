package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

import java.io.Serializable;
import java.util.Optional;

@Data
@Builder
@EqualsAndHashCode
public class PayForTheBlueBadgeRetryForm implements StepForm, Serializable {

  private Boolean retry;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.PAY_FOR_THE_BLUE_BADGE_RETRY;
  }

  @Override
  public Optional<StepDefinition> determineNextStep(Journey journey) {
    //Assert.isTrue(!payNow, "payNow should be false at this point");
    if (!retry) {
      return Optional.of(StepDefinition.SUBMITTED);
    } else {
      return Optional.of(StepDefinition.PAY_FOR_THE_BLUE_BADGE_RETURN); // TODO: review, this is a hack
    }
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return true;
  }
}
