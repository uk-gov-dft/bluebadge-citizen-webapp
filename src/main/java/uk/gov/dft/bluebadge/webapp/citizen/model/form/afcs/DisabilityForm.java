package uk.gov.dft.bluebadge.webapp.citizen.model.form.afcs;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;
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

    return Optional.of(StepDefinition.MAIN_REASON);
  }

  @Override
  public Set<StepDefinition> getCleanUpSteps(Journey journey) {
    return getAssociatedStep().getNext();
  }
}
