package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import com.google.common.collect.ImmutableList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Builder
@Slf4j
public class JourneySpecification {
  @Getter private JourneySection preApplicationJourney;
  @Getter private JourneySection submitAndPayJourney;
  @Getter private Map<EligibilityCodeField, JourneySection> eligibilityCodeToJourneyMap;

  public JourneySection getApplicationSection(Journey journey){
    return determineApplicationSection(journey);
  }

  List<JourneySection> getFullJourney(Journey journey) {
    ImmutableList.Builder<JourneySection> result =
        ImmutableList.<JourneySection>builder().add(preApplicationJourney);

    JourneySection applicationJourney = determineApplicationSection(journey);
    if (null != applicationJourney) {
      result.add(applicationJourney);
    }

    result.add(submitAndPayJourney);

    return result.build();
  }

  public StepDefinition determineNextStep(Journey journey, StepDefinition currentStep) {
    List<JourneySection> fullJourney = getFullJourney(journey);

    Task task = determineTask(fullJourney, currentStep);
    StepDefinition result = task.getNextStep(journey, currentStep);

    //  Return here once Task List page completed.
    return result;

    // TMP
//    while (null == result) {
//      task = determineNextTask(fullJourney, task);
//      result = task.getFirstStep(journey);
//    }
//    return result;
  }

  private JourneySection determineApplicationSection(Journey journey) {
    EligibilityCodeField eligibilityCode = journey.getEligibilityCode();
    if (null != eligibilityCode) {
      return eligibilityCodeToJourneyMap.get(eligibilityCode);
    }
    return null;
  }

  /** @throws IllegalStateException If step not part of the journey */
  Task determineTask(Journey journey, StepDefinition step) throws IllegalStateException {
    List<JourneySection> fullJourney = getFullJourney(journey);
    return determineTask(fullJourney, step);
  }

  private Task determineTask(List<JourneySection> fullJourney, StepDefinition step) {
    return fullJourney
        .stream()
        .filter(Objects::nonNull)
        .map(jd -> jd.findTaskByStep(step))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .findAny() // Ok because tasks never duplicated across journeys
        .orElseThrow(
            () ->
                new IllegalStateException(
                    "Can't find task, step " + step + " not part of journey:" + fullJourney));
  }

  // Temp until we have the TaskDefinition List page
  // Get the next task, or the first task of the next journey
  private Task determineNextTask(List<JourneySection> fullJourney, Task currentTask) {
    for (Iterator<JourneySection> iterator = fullJourney.iterator(); iterator.hasNext(); ) {
      JourneySection jd = iterator.next();
      if (jd.getTasks().contains(currentTask)) {
        Task result = jd.findNextTask(currentTask);
        if (null != result) {
          return result;
        } else if (iterator.hasNext()) {
          return iterator.next().getTasks().get(0);
        }
      }
    }
    throw new IllegalStateException("No next task.");
  }

  public boolean arePreviousSectionsComplete(Journey journey, Task task) {
    if (preApplicationJourney.getTasks().contains(task)) {
      return true;
    } else if (!preApplicationJourney.isComplete(journey)) {
      return false;
    }

    JourneySection applicationSection = determineApplicationSection(journey);
    if (null == applicationSection) {
      throw new IllegalStateException(
          "No application journey found for Eligibility code:" + journey.getEligibilityCode());
    }
    if (applicationSection.getTasks().contains(task)) {
      return true;
    } else if (!applicationSection.isComplete(journey)) {
      return false;
    }

    if (submitAndPayJourney.getTasks().contains(task)) {
      // TODO error
      return true;
    } else if (!preApplicationJourney.isComplete(journey)) {
      return false;
    }

    // error
    return true;
  }
}
