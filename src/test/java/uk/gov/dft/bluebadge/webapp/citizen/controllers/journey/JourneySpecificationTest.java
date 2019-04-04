package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.dft.bluebadge.webapp.citizen.BaseSpringBootTest;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.tasks.TaskConfigurationException;
import uk.gov.dft.bluebadge.webapp.citizen.fixture.JourneyFixture;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

class JourneySpecificationTest extends BaseSpringBootTest {
  @Autowired JourneySpecification journeySpecification;
  private Task declarationTask;
  private Task personalDetailsTask;
  private Task checkEligTask;

  @BeforeEach
  public void setup(){
    checkEligTask = journeySpecification.getPreApplicationJourney().getTasks().get(0);
    JourneySection journeySection =
        journeySpecification.getEligibilityCodeToJourneyMap().get(EligibilityCodeField.WALKD);
    personalDetailsTask = journeySection.findTaskByStep(StepDefinition.GENDER).get();
    declarationTask = journeySpecification.getSubmitAndPayJourney().getTasks().get(0);
  }

  @Test
  void preApplicationJourneyTasksNotShared() {
    JourneySection preApplicationJourney = journeySpecification.getPreApplicationJourney();

    Collection<JourneySection> journeySections =
        journeySpecification.getEligibilityCodeToJourneyMap().values();
    ImmutableList<JourneySection> others =
        ImmutableList.<JourneySection>builder()
            .addAll(journeySections)
            .add(journeySpecification.getSubmitAndPayJourney())
            .build();

    Set<Task> duplicates =
        others
            .stream()
            .flatMap(js -> js.getTasks().stream())
            .filter(t -> preApplicationJourney.getTasks().contains(t))
            .collect(Collectors.toSet());

    assertThat(duplicates).isEmpty();
  }

  @Test
  void applyJourneyTasksNotShared() {
    JourneySection submitAndPayJourney = journeySpecification.getSubmitAndPayJourney();

    Collection<JourneySection> journeySections =
        journeySpecification.getEligibilityCodeToJourneyMap().values();
    ImmutableList<JourneySection> others =
        ImmutableList.<JourneySection>builder()
            .add(journeySpecification.getPreApplicationJourney())
            .addAll(journeySections)
            .build();

    Set<Task> duplicates =
        others
            .stream()
            .flatMap(js -> js.getTasks().stream())
            .filter(t -> submitAndPayJourney.getTasks().contains(t))
            .collect(Collectors.toSet());

    assertThat(duplicates).isEmpty();
  }

  @Test
  void journeyTasksContainNoDuplicateSteps() {
    Collection<JourneySection> journeySections =
        journeySpecification.getEligibilityCodeToJourneyMap().values();

    Set<StepDefinition> tmp = new HashSet<>();
    for (JourneySection js : journeySections) {
      ImmutableList<JourneySection> fullJourney =
          ImmutableList.<JourneySection>builder()
              .add(journeySpecification.getPreApplicationJourney())
              .add(js)
              .add(journeySpecification.getSubmitAndPayJourney())
              .build();

      tmp.clear();
      Set<StepDefinition> duplicates =
          fullJourney
              .stream()
              .flatMap(j -> j.getTasks().stream())
              .flatMap(t -> t.getSteps().stream())
              .filter(s -> !tmp.add(s))
              .collect(Collectors.toSet());
      assertThat(duplicates)
          .describedAs("Full journey %s, has duplicate steps.", fullJourney)
          .isEmpty();
    }
  }

  @Test
  void arePreviousSectionsComplete_whenStartingJourney() {
    Journey journey = JourneyFixture.getDefaultJourneyToStep(StepDefinition.APPLICANT_TYPE);

    assertThat(journeySpecification.arePreviousSectionsComplete(journey, checkEligTask)).isTrue();
    assertThat(journeySpecification.arePreviousSectionsComplete(journey, personalDetailsTask))
        .isFalse();
    assertThat(journeySpecification.arePreviousSectionsComplete(journey, declarationTask))
        .isFalse();
  }

  @Test
  void arePreviousSectionsComplete_whenInApplicationSection() {
    Journey journey = JourneyFixture.getDefaultJourneyToStep(StepDefinition.NAME);

    assertThat(journeySpecification.arePreviousSectionsComplete(journey, checkEligTask)).isTrue();
    assertThat(journeySpecification.arePreviousSectionsComplete(journey, personalDetailsTask))
        .isTrue();
    assertThat(journeySpecification.arePreviousSectionsComplete(journey, declarationTask))
        .isFalse();
  }

  @Test
  void arePreviousSectionsComplete_whenInApplySection() {
    Journey journey = JourneyFixture.getDefaultJourneyToStep(StepDefinition.DECLARATIONS);

    assertThat(journeySpecification.arePreviousSectionsComplete(journey, checkEligTask)).isTrue();
    assertThat(journeySpecification.arePreviousSectionsComplete(journey, personalDetailsTask))
        .isTrue();
    assertThat(journeySpecification.arePreviousSectionsComplete(journey, declarationTask)).isTrue();
  }

  @Test()
  void arePreviousSectionsComplete_whenUnknownTaskForJourneyThenException() {
    Journey journey = JourneyFixture.getDefaultJourneyToStep(StepDefinition.DECLARATIONS);

    JourneySection journeySection =
        journeySpecification.getEligibilityCodeToJourneyMap().get(EligibilityCodeField.WALKD);
    Task walkingTimeTask = journeySection.findTaskByStep(StepDefinition.WALKING_TIME).get();

    assertThat(walkingTimeTask).isNotNull();
    assertThrows(
        TaskConfigurationException.class,
        () -> journeySpecification.arePreviousSectionsComplete(journey, walkingTimeTask));
  }
}
