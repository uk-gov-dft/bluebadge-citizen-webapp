package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.tasks;

import com.google.common.collect.ImmutableList;
import java.util.List;
import lombok.Getter;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Task;

public class SimpleTask implements Task {
  @Getter private final String titleCode;
  @Getter private final List<StepDefinition> steps;

  public SimpleTask(String titleCode, StepDefinition... steps) {
    this.titleCode = titleCode;
    this.steps = ImmutableList.copyOf(steps);
  }
}
