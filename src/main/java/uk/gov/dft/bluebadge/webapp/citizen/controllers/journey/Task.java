package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.tasks.TaskConfigurationException;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Getter
@EqualsAndHashCode
public abstract class Task {
  enum TaskState{
    NOT_STARTED, IN_PROGRESS, COMPLETED
  }

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
            throw new TaskConfigurationException(
                "Step form: " + form + ", returned a step not within the task. " + nextStep);
          }
          return nextStep;
        }
      }
      return steps.get(steps.indexOf(current) + 1);
    } catch (IndexOutOfBoundsException e) {
      // End of steps for this task
      return null;
    }
  }

  /**
   * @param journey The journey to determine the first step
   * @return The first step for the task, given the specific journey
   */
  public StepDefinition getFirstStep(Journey journey) {
    return getSteps().get(0);
  }

  public boolean isComplete(Journey journey) {
    return TaskState.COMPLETED == getState(journey);
  }

  public TaskState getState(Journey journey){
    StepDefinition step = getFirstStep(journey);
    boolean foundOne = false;
    while (null != step) {
      if (null == journey.getFormForStep(step)) {
        return foundOne ? TaskState.IN_PROGRESS : TaskState.NOT_STARTED;
      }
      step = this.getNextStep(journey, step);
    }
    return TaskState.COMPLETED;
  }
}
