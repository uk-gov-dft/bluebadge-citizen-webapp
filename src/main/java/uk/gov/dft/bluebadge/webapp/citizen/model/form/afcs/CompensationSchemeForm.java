package uk.gov.dft.bluebadge.webapp.citizen.model.form.afcs;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.Nation;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Data
@Builder
@EqualsAndHashCode
public class CompensationSchemeForm implements Serializable, StepForm {

  @NotNull(message = "{NotNull.hasReceivedCompensation}")
  private Boolean hasReceivedCompensation;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.AFCS_COMPENSATION_SCHEME;
  }

  @Override
  public Optional<StepDefinition> determineNextStep(Journey journey) {
    Nation nation = journey.getLocalAuthority().getNation();

    if (hasReceivedCompensation) {
      return Optional.of(StepDefinition.AFCS_DISABILITY);
    }

    if (Nation.WLS.equals(nation)) {
      return Optional.of(StepDefinition.AFCS_MENTAL_DISORDER);
    }

    return Optional.of(StepDefinition.MAIN_REASON);
  }

  @Override
  public Set<StepDefinition> getCleanUpSteps(Journey journey) {
    return getAssociatedStep().getNext();
  }
}
