package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.tasks;

import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingLengthOfTimeCodeField.CANTWALK;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.WALKING_TIME;

import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Task;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.WalkingTimeForm;

@EqualsAndHashCode(callSuper = true)
public class WalkingTask extends Task {

  public WalkingTask(String titleCode, StepDefinition... steps) {
    super(titleCode, steps);
  }

  @Override
  public StepDefinition getNextStep(Journey journey, StepDefinition current) {
    // Early exit points
    if (WALKING_TIME == current) {
      WalkingTimeForm walkingTimeForm = journey.getFormForStep(current);
      if (CANTWALK == walkingTimeForm.getWalkingTime()) {
        return null;
      }
    }

    return super.getNextStep(journey, current);
  }
}
