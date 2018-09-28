package uk.gov.dft.bluebadge.webapp.citizen.model.form.afcs;

import java.io.Serializable;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

@Data
@Builder
public class DisabilityForm implements Serializable, StepForm {

  @NotNull(message = "{NotNull.hasDisability}")
  private Boolean hasDisability;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.AFCS_DISABILITY;
  }

  @Override
  public Optional<StepDefinition> determineNextStep() {
    if (hasDisability) {
      return Optional.of(StepDefinition.ELIGIBLE);
    }

    return Optional.of(StepDefinition.MAY_BE_ELIGIBLE);
  }
}
