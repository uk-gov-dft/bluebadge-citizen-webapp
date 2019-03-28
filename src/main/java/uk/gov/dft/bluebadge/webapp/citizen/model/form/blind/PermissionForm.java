package uk.gov.dft.bluebadge.webapp.citizen.model.form.blind;

import java.io.Serializable;

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
public class PermissionForm implements Serializable, StepForm {

  @NotNull(message = "{NotNull.hasPermission}")
  private Boolean hasPermission;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.PERMISSION;
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return false;
  }
}
