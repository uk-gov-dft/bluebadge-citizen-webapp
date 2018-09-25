package uk.gov.dft.bluebadge.webapp.citizen.model.form.AFCS;

import java.io.Serializable;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

@Data
@Builder
public class CompensationSchemeForm implements Serializable, StepForm {

  @NotNull(message = "{NotNull.hasReceivedCompensation}")
  private Boolean hasReceivedCompensation;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.AFCS_COMPENSATION_SCHEME;
  }

  @Override
  public Optional<StepDefinition> determineNextStep() {
    String wal = "Wales";
    String nation = "Wales";

    if (hasReceivedCompensation) {
      return Optional.of(StepDefinition.AFCS_DISABILITY);
    }

    if (nation.equals(wal)) {
      return Optional.of(StepDefinition.AFCS_MENTAL_DISORDER);
    }

    return Optional.of(StepDefinition.MAY_BE_ELIGIBLE);
  }
}
