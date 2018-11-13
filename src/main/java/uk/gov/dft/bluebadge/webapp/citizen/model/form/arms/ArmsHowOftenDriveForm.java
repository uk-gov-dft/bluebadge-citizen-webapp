package uk.gov.dft.bluebadge.webapp.citizen.model.form.arms;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
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
public class ArmsHowOftenDriveForm implements Serializable, StepForm {

  @NotBlank
  @Size(max = 100)
  private String howOftenDrive;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.ARMS_HOW_OFTEN_DRIVE;
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return false;
  }
}
