package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.tasks;

import com.google.common.collect.ImmutableList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Task;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@EqualsAndHashCode
public class SubmitAndPayTask implements Task {

  @Getter private final String titleCode;
  @Getter private final List<StepDefinition> steps;

  public SubmitAndPayTask(String titleCode, StepDefinition... steps) {
    this.titleCode = titleCode;
    this.steps = ImmutableList.copyOf(steps);
  }

  @Override
  public StepDefinition getFirstStep(Journey journey) {
    if (journey.isPaymentsEnabled()) {
      return StepDefinition.BADGE_PAYMENT;
    } else {
      return StepDefinition.SUBMITTED;
    }
  }
}
