package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class TaskTest {

  @ParameterizedTest(name = "Steps within task have next steps only within task - {0}")
  @EnumSource(Task.class)
  public void taskHaveNoStepsWithNextStepsOutsideOfTheTask(Task task) {
    List<StepDefinition> taskSteps = task.getSteps();
    Set<StepDefinition> outOfTaskSteps =
        taskSteps
            .stream()
            .flatMap(s -> s.getNext().stream())
            .filter(s -> !taskSteps.contains(s))
            .collect(Collectors.toSet());

    assertThat(outOfTaskSteps).isEmpty();
  }

  @Test
  void orphanedSteps() {
    Set<StepDefinition> allTaskSteps =
        Arrays.stream(Task.values())
            .flatMap(t -> t.getSteps().stream())
            .collect(Collectors.toSet());

    Set<StepDefinition> orpahned =
        Arrays.stream(StepDefinition.values())
            .filter(s -> !allTaskSteps.contains(s))
            .collect(Collectors.toSet());

    assertThat(orpahned).containsOnly(StepDefinition.SUBMITTED, StepDefinition.TASK_LIST);
  }
}
