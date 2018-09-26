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
  GENDER(HEALTH_CONDITIONS),
  DOB(GENDER),
  NAME(DOB),
  ELIGIBLE(NAME),
  MAY_BE_ELIGIBLE(NAME),
  HIGHER_RATE_MOBILITY(ELIGIBLE, MAY_BE_ELIGIBLE),
  PIP_DLA(ELIGIBLE, MAY_BE_ELIGIBLE),
  PIP_PLANNING_JOURNEY(PIP_DLA, ELIGIBLE, MAY_BE_ELIGIBLE),
  PIP_MOVING_AROUND(PIP_PLANNING_JOURNEY, ELIGIBLE, MAY_BE_ELIGIBLE),
  RECEIVE_BENEFITS(ELIGIBLE, MAY_BE_ELIGIBLE, PIP_MOVING_AROUND, HIGHER_RATE_MOBILITY),
  YOUR_ISSUING_AUTHORITY(RECEIVE_BENEFITS),
  CHOOSE_COUNCIL(YOUR_ISSUING_AUTHORITY),
  APPLICANT_TYPE(CHOOSE_COUNCIL),
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
