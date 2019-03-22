package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.JourneyDefinition.J_APPLY;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.JourneyDefinition.J_PRE_APPLICATION;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Slf4j
@Component
public class JourneyMaster {
  public StepDefinition determineNextStep(Journey journey, StepDefinition currentStep) {
    JourneyDefinition journeyDefinition = journey.getJourneyDefinition();
    List<JourneyDefinition> fullJourney = new ArrayList<>();
    fullJourney.add(J_PRE_APPLICATION);
    fullJourney.add(journeyDefinition);
    fullJourney.add(J_APPLY);
    return determineNextStep(journey, fullJourney, currentStep);
  }

  private StepDefinition determineNextStep(
      Journey journey, List<JourneyDefinition> fullJourney, StepDefinition currentStep) {
    StepDefinition nextStep =
        fullJourney
            .stream()
            .filter(Objects::nonNull)
            .map(jd -> jd.findTaskByStep(currentStep))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findFirst() // Ok because tasks never duplicated across journeys
            .orElseThrow(
                () ->
                    new IllegalStateException(
                        "Current step " + currentStep + " not part of this journey:" + fullJourney))
            .getNextStep(journey, currentStep);

    if (null == nextStep) {
      //      nextStep = TASK_LIST; // End of task. So show task list.

      // Tmp until task list implemented:
      Task task = getCurrentTask(fullJourney, currentStep);
      while (null == nextStep) { // Incase the next task says no (eg Provide Address)
        task = getNextTask(fullJourney, task);
        nextStep = task.getFirstStep(journey);
      }
    }
    return nextStep;
  }

  // Temp until we have the Task List page
  private Task getCurrentTask(List<JourneyDefinition> fullJourney, StepDefinition currentStep) {
    Iterator<JourneyDefinition> iterator = fullJourney.iterator();
    while (iterator.hasNext()) {
      JourneyDefinition jd = iterator.next();
      Optional<Task> currentTask = jd.findTaskByStep(currentStep);
      if (currentTask.isPresent()) {
        return currentTask.get();
      }
    }
    throw new IllegalStateException("Can't find current task for step:" + currentStep);
  }

  // Temp until we have the Task List page
  // Get the next task, or the first task of the next journey
  private Task getNextTask(List<JourneyDefinition> fullJourney, Task currentTask) {
    Iterator<JourneyDefinition> iterator = fullJourney.iterator();
    while (iterator.hasNext()) {
      JourneyDefinition jd = iterator.next();
      if (jd.getTasks().contains(currentTask)) {
        int i = jd.getTasks().indexOf(currentTask);
        try {
          return jd.getTasks().get(i + 1);
        } catch (IndexOutOfBoundsException e) {
          return iterator.next().getTasks().get(0);
        }
      }
    }
    throw new IllegalStateException("No next task.");
  }
  //
  //  void printWhereWeAre(StepDefinition current, StepDefinition next) {
  //    JourneyDefinition journeyDefinition = journey.getJourneyDefinition();
  //
  ////    Optional<Task> currentTask = journeyDefinition.findTaskByStep(current);
  ////    Optional<Task> nextTask = journeyDefinition.findTaskByStep(next);
  ////
  ////    log.info("Current journey definition: {}", journeyDefinition);
  ////    log.info("Task: {}  for current step: {}", currentTask, current);
  ////    log.info("Task: {} for next step: {}", nextTask, next);
  //
  ////    if (J_PRE_APPLICATION == journeyDefinition) {
  ////      if (ELIGIBLE == current || MAY_BE_ELIGIBLE == current) {
  ////        onCompletePreApplication();
  ////      }
  ////    }
  //  }

}
