package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.tasks;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.PERMISSION;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.REGISTERED;

import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Task;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.blind.PermissionForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.blind.RegisteredForm;

@EqualsAndHashCode(callSuper = true)
public class BlindTask extends Task {

  public BlindTask(String titleCode, StepDefinition... steps) {
    super(titleCode, steps);
  }

  @Override
  public StepDefinition getNextStep(Journey journey, StepDefinition current) {
    // Early exit points
    if (REGISTERED == current) {
      RegisteredForm registeredForm = journey.getFormForStep(current);
      if (!registeredForm.getHasRegistered()) {
        return null;
      }
    } else if (PERMISSION == current) {
      PermissionForm permissionForm = journey.getFormForStep(current);
      if (!permissionForm.getHasPermission()) {
        return null;
      }
    }

    return super.getNextStep(journey, current);
  }
}
