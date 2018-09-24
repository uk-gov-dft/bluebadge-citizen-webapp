package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

import java.io.Serializable;
import java.util.Optional;

@Data
@Builder
public class PipDlaQuestionForm implements Serializable, StepForm {
  public enum PipReceivedDlaOption {
    HAS_RECEIVED_DLA,
    NEVER_RECEIVED_DLA
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

  private PipMovingAroundForm.PipMovingAroundOption movingAroundPoints;
}
