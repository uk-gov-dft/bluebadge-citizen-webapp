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
  NAME(HEALTH_CONDITIONS),
  ELIGIBLE(NAME),
  MAY_BE_ELIGIBLE(NAME),
  RECEIVE_BENEFITS(ELIGIBLE, MAY_BE_ELIGIBLE),
  APPLICANT_TYPE(RECEIVE_BENEFITS),
  HOME(APPLICANT_TYPE);

  private Set<StepDefinition> next;

  StepDefinition(StepDefinition... whereNext) {
    next = Stream.of(whereNext).collect(Collectors.toSet());
  }

  public StepDefinition getDefaultNext() {
    if (next.size() != 1) {
      throw new IllegalStateException(
          "Failed to determine single next step for current step:" + this + ". Got:" + next);
    }
    return next.iterator().next();
  }
}
