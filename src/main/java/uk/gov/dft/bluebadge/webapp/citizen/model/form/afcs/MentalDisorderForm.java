package uk.gov.dft.bluebadge.webapp.citizen.model.form.afcs;

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

    return Optional.of(StepDefinition.MAIN_REASON);
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return false;
  }
}
