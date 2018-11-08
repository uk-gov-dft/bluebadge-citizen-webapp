package uk.gov.dft.bluebadge.webapp.citizen.model.form.arms;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@EqualsAndHashCode
@Data
@Builder
public class ArmsDifficultyParkingMetersForm implements Serializable, StepForm {

  @NotBlank
  @Size(max = 1000)
  private String parkingMetersDifficultyDescription;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.ARMS_DIFFICULTY_PARKING_METER;
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return false;
  }
}
