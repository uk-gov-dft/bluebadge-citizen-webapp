package uk.gov.dft.bluebadge.webapp.citizen.model.form.pip;

import static uk.gov.dft.bluebadge.webapp.citizen.model.form.pip.PipDlaQuestionForm.PipReceivedDlaOption.HAS_RECEIVED_DLA;
import static uk.gov.dft.bluebadge.webapp.citizen.model.form.pip.PipDlaQuestionForm.PipReceivedDlaOption.NEVER_RECEIVED_DLA;

import java.io.Serializable;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Data
@Builder
@EqualsAndHashCode
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
      return Optional.of(StepDefinition.MAIN_REASON);
    }
    return Optional.empty();
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return false;
  }

  @NotNull private PipDlaQuestionForm.PipReceivedDlaOption receivedDlaOption;
}
