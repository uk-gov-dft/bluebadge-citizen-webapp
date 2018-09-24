package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

import java.io.Serializable;
import java.util.Optional;

@Data
@Builder
public class PipMovingAroundForm implements Serializable, StepForm {
  public enum PipMovingAroundOption {
    MOVING_POINTS_12,
    MOVING_POINTS_10,
    MOVING_POINTS_8,
    MOVING_POINTS_4,
    MOVING_POINTS_0
  }

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.PIP_MOVING_AROUND;
  }

  @Override
  public Optional<StepDefinition> determineNextStep() {
    // TODO
    return Optional.of(StepDefinition.ELIGIBLE);
  }

  private PipMovingAroundOption movingAroundPoints;
}
