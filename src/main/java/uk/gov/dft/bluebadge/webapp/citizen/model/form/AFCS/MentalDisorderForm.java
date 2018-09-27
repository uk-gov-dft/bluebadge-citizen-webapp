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
public class MentalDisorderForm implements Serializable, StepForm {

  @NotNull(message = "{NotNull.hasMentalDisorder}")
  private Boolean hasMentalDisorder;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.AFCS_MENTAL_DISORDER;
  }

  @Override
  public Optional<StepDefinition> determineNextStep() {
    if (hasMentalDisorder) {
      return Optional.of(StepDefinition.ELIGIBLE);
    }

    return Optional.of(StepDefinition.MAY_BE_ELIGIBLE);
  }
}
