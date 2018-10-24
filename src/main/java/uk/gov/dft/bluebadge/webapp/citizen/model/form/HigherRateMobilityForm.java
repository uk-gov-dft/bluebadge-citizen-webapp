package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

@Data
@Builder
@EqualsAndHashCode
public class HigherRateMobilityForm implements StepForm, Serializable {

  @NotNull(message = "{NotBlank.awardedHigherRateMobility}")
  private Boolean awardedHigherRateMobility;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.HIGHER_RATE_MOBILITY;
  }

  @Override
  public Optional<StepDefinition> determineNextStep() {
    if (awardedHigherRateMobility) {
      return Optional.of(StepDefinition.ELIGIBLE);
    } else {
      return Optional.of(StepDefinition.MAIN_REASON);
    }
  }
}
