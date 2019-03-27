package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.dft.bluebadge.webapp.citizen.BaseSpringBootTest;

class JourneySpecificationTest extends BaseSpringBootTest {
  @Autowired JourneySpecification journeySpecification;

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
}
