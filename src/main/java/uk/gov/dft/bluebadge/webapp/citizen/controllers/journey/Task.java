package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Getter
@EqualsAndHashCode
public abstract class Task {
  private final String titleCode;
  private final List<StepDefinition> steps;

  protected Task(String titleCode, StepDefinition... steps) {
    this.titleCode = titleCode;
    this.steps = ImmutableList.copyOf(steps);
  }

  public StepDefinition getNextStep(Journey journey, StepDefinition current) {
    try {
      StepForm form = journey.getFormForStep(current);
      if (null != form) {
        Optional<StepDefinition> stepDefinition = form.determineNextStep(journey);
        if (stepDefinition.isPresent()) {
          StepDefinition nextStep = stepDefinition.get();
          if (!getSteps().contains(nextStep)) {
            throw new RuntimeException(
                "Step form: " + form + ", returned a step not within the task. " + nextStep);
          }
          return nextStep;
        }
      }
      List<StepDefinition> steps = getSteps();
      return steps.get(steps.indexOf(current) + 1);
    } catch (IndexOutOfBoundsException e) {
      // End of steps for this task
      return null;
    }
  }

  public StepDefinition getFirstStep(Journey journey) {
    return getSteps().get(0);
  }

  public boolean isComplete(Journey journey) {
    StepDefinition step = getFirstStep(journey);
    while (null != step) {
      if (null == journey.getFormForStep(step)) {
        return false;
      }
      step = this.getNextStep(journey, step);
    }
    return true;
  }
}
