package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.HOME;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.MEDICATION_ADD;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.MOBILITY_AID_ADD;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.PAY_FOR_THE_BADGE_RETURN;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.TREATMENT_ADD;

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

    assertThat(unreachable)
        .containsOnly(
            HOME,
            MOBILITY_AID_ADD,
            TREATMENT_ADD,
            MEDICATION_ADD,
            PAY_FOR_THE_BADGE_RETURN,
            StepDefinition.HEALTHCARE_PROFESSIONALS_ADD);
  }
}
