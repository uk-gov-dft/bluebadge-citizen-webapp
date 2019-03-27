package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.ToString;

@ToString(of = "name")
public class JourneySection {
  @Getter private final String name;
  @Getter private final List<Task> tasks;

  public JourneySection(String name, Task... tasks) {
    this.name = name;
    this.tasks = ImmutableList.copyOf(tasks);
  }

  /**
   * Safe to assume that this can only return one TaskDefinition. As a step can never be repeated
   * within a journey, nor a task
   */
  public Optional<Task> findTaskByStep(StepDefinition stepDefinition) {
    return this.getTasks().stream().filter(t -> t.getSteps().contains(stepDefinition)).findFirst();
  }

  public Task findNextTask(Task currentTask) {
    int tmp = tasks.indexOf(currentTask);
    try {
      return tasks.get(tmp + 1);
    } catch (IndexOutOfBoundsException e) {
      return null;
    }
  }
}
