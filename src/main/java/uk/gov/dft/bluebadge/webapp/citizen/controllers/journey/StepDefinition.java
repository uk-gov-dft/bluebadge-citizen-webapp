package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum StepDefinition {
  SUBMITTED(),
  DECLARATIONS(SUBMITTED),
  HEALTH_CONDITIONS(DECLARATIONS),
  YOUR_ISSUING_AUTHORITY(HEALTH_CONDITIONS),
  CHOOSE_COUNCIL(YOUR_ISSUING_AUTHORITY),
  APPLICANT_TYPE(CHOOSE_COUNCIL),
  HOME(APPLICANT_TYPE);

  private Set<StepDefinition> next;

  StepDefinition(StepDefinition... whereNext) {
    next = Stream.of(whereNext).collect(Collectors.toSet());
  }
}
