package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.tasks.TaskConfigurationException;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@EqualsAndHashCode
@Slf4j
public abstract class Task {
  enum TaskState{
    NOT_STARTED, IN_PROGRESS, COMPLETED
  }

  private final String titleCode;
  @Getter private final List<StepDefinition> steps;

  protected Task(String titleCode, StepDefinition... steps) {
    this.titleCode = titleCode;
    this.steps = ImmutableList.copyOf(steps);
  }

  public String getTitleCode(Journey journey){
    return titleCode;
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
      foundOne = true;
      step = this.getNextStep(journey, step);
    }
    return TaskState.COMPLETED;
  }

  /**
   * Default method for determining if the step within the task is valid for the given journey.
   * @return false if step not within task. false is any prior step does not have a form within
   * the journey
   */
  public boolean isValidStep(Journey journey, StepDefinition step) {
    StepDefinition currentLoopStep = this.getFirstStep(journey);
    int stepsWalked = 0;
    while (true) {

      if (currentLoopStep == step) {
        // Got to step being validated in journey, so it is valid.
        return true;
      }

      // Should not need next...but don't want an infinite loop.
      if (stepsWalked++ > this.getSteps().size()) {
        log.error("IsValidState journey walk got into infinite loop. Step being checked {}.", step);
        throw new IllegalStateException();
      }

      if (!journey.hasStepForm(currentLoopStep)) {
        return false;
      }

      StepDefinition nextStep;

      StepForm currentLoopForm = journey.getFormForStep(currentLoopStep);
      nextStep = getNextStep(journey, currentLoopForm.getAssociatedStep());
      if (null == nextStep) {
        // Got to the end of the task without finding the step.
        // TODO Error!!
        return false;
      }

      currentLoopStep = nextStep;
    }
  }
}
