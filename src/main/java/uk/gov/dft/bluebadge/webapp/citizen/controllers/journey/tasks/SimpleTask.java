package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.tasks;

import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Task;

@EqualsAndHashCode(callSuper = true)
public class SimpleTask extends Task {

  public SimpleTask(String titleCode, StepDefinition... steps) {
    super(titleCode, steps);
  }
}
