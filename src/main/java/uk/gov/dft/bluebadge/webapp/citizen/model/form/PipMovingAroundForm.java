package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

import javax.validation.constraints.NotNull;
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
    switch (movingAroundPoints) {
      case MOVING_POINTS_8:
      case MOVING_POINTS_10:
      case MOVING_POINTS_12:
        return Optional.of(StepDefinition.ELIGIBLE);
      case MOVING_POINTS_0:
      case MOVING_POINTS_4:
        // TODO England path!!!!
        return Optional.of(StepDefinition.PIP_PLANNING_JOURNEY);
    }
    return Optional.empty();
  }

  @NotNull private PipMovingAroundOption movingAroundPoints;
}
