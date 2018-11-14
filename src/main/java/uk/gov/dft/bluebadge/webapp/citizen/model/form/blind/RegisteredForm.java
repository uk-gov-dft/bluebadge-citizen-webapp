package uk.gov.dft.bluebadge.webapp.citizen.model.form.blind;

import java.io.Serializable;
import java.util.Optional;
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
public class RegisteredForm implements Serializable, StepForm {

  @NotNull(message = "{NotNull.hasRegistered}")
  private Boolean hasRegistered;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.REGISTERED;
  }

  @Override
  public Optional<StepDefinition> determineNextStep(Journey journey) {
    Nation nation = journey.getLocalAuthority().getNation();

    if (hasRegistered) {
      return Optional.of(StepDefinition.PERMISSION);
    }
    return Optional.of(StepDefinition.DECLARATIONS);
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return false;
  }
}
