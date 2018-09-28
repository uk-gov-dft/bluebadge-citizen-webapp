package uk.gov.dft.bluebadge.webapp.citizen.model.form.pip;

import static uk.gov.dft.bluebadge.webapp.citizen.model.form.pip.PipDlaQuestionForm.PipReceivedDlaOption.HAS_RECEIVED_DLA;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.pip.PipDlaQuestionForm.PipReceivedDlaOption.NEVER_RECEIVED_DLA;

import java.io.Serializable;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

@Data
@Builder
public class PipDlaQuestionForm implements Serializable, StepForm {
  public enum PipReceivedDlaOption {
    HAS_RECEIVED_DLA,
    NEVER_RECEIVED_DLA
  }

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.PIP_DLA;
  }

  @Override
  public Optional<StepDefinition> determineNextStep() {
    if (HAS_RECEIVED_DLA.equals(receivedDlaOption)) {
      return Optional.of(StepDefinition.ELIGIBLE);
    }
    if (NEVER_RECEIVED_DLA.equals(receivedDlaOption)) {
      return Optional.of(StepDefinition.MAY_BE_ELIGIBLE);
    }
    return Optional.empty();
  }

  @NotNull private PipDlaQuestionForm.PipReceivedDlaOption receivedDlaOption;
}
