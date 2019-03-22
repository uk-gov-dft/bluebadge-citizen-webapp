package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.JourneyDefinition.J_APPLY;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.JourneyDefinition.J_PRE_APPLICATION;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class JourneyDefinitionTest {

  @ParameterizedTest(name = "Duplicate Tasks within Journey Definition - {0}")
  @EnumSource(JourneyDefinition.class)
  public void journeyDefinitionsContainNoDuplicateTasks(JourneyDefinition journeyDefinition) {
    Set<Task> tmp = new HashSet<>();
    Set<Task> duplicates =
        journeyDefinition.getTasks().stream().filter(t -> !tmp.add(t)).collect(Collectors.toSet());

    assertThat(duplicates).isEmpty();
  }

  @Test
  void preApplicationJourneyTasksNotShared() {
    EnumSet<JourneyDefinition> others = EnumSet.complementOf(EnumSet.of(J_PRE_APPLICATION));
    Set<Task> duplicates =
        others
            .stream()
            .flatMap(jd -> jd.getTasks().stream())
            .filter(t -> J_PRE_APPLICATION.getTasks().contains(t))
            .collect(Collectors.toSet());

    assertThat(duplicates).isEmpty();
  }

  @Test
  void applyJourneyTasksNotShared() {
    EnumSet<JourneyDefinition> others = EnumSet.complementOf(EnumSet.of(J_APPLY));
    Set<Task> duplicates =
        others
            .stream()
            .flatMap(jd -> jd.getTasks().stream())
            .filter(t -> J_APPLY.getTasks().contains(t))
            .collect(Collectors.toSet());

    assertThat(duplicates).isEmpty();
  }

  @ParameterizedTest(name = "Duplicate Steps within Journey Definition - {0}")
  @EnumSource(JourneyDefinition.class)
  public void journeyTasksContainNoDuplicateSteps(JourneyDefinition journeyDefinition) {
    Set<StepDefinition> tmp = new HashSet<>();
    Set<StepDefinition> duplicates =
        journeyDefinition
            .getTasks()
            .stream()
            .map(Task::getSteps)
            .flatMap(Collection::stream)
            .filter(s -> !tmp.add(s))
            .collect(Collectors.toSet());

    assertThat(duplicates).isEmpty();
  }
}
