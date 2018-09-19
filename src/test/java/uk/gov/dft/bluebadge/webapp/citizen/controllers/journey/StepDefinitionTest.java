package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;

public class StepDefinitionTest {

  @Test
  public void unreachableSteps() {
    Set<StepDefinition> allNextSteps =
        Stream.of(StepDefinition.values())
            .flatMap(s -> s.getNext().stream())
            .collect(Collectors.toSet());

    Set<StepDefinition> unreachable =
        Stream.of(StepDefinition.values())
            .filter(s -> !allNextSteps.contains(s))
            .collect(Collectors.toSet());

    assertThat(unreachable).containsOnly(StepDefinition.HOME);
  }
}
