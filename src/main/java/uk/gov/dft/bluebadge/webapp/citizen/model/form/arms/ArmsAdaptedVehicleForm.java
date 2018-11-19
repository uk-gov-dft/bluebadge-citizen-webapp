package uk.gov.dft.bluebadge.webapp.citizen.model.form.arms;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@EqualsAndHashCode
@Data
@Builder
public class ArmsAdaptedVehicleForm implements Serializable, StepForm {

  @NotNull private Boolean hasAdaptedVehicle;

  @Size(max = 255)
  private String adaptedVehicleDescription;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.ARMS_DRIVE_ADAPTED_VEHICLE;
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return false;
  }
}
