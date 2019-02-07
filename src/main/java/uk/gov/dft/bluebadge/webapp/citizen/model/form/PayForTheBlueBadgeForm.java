package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

import javax.validation.constraints.AssertTrue;

@Data
@Builder
@EqualsAndHashCode
public class PayForTheBlueBadgeForm implements StepForm, Serializable {

  private Boolean payNow;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.PAY_FOR_THE_BLUE_BADGE;
  }

  @Override
  public Optional<StepDefinition> determineNextStep(Journey journey) {
    Assert.isTrue(!payNow, "payNow should be false at this point");
    if (!payNow) {
      return Optional.of(StepDefinition.SUBMITTED);
    } else {
      return Optional.empty();
    }
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return true;
  }
}
