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
  CONTACT_DETAILS(HEALTH_CONDITIONS, DECLARATIONS),
  ADDRESS(CONTACT_DETAILS),
  NINO(ADDRESS),
  GENDER(NINO, ADDRESS),
  DOB(GENDER),
  NAME(DOB),
  ELIGIBLE(NAME),
  MAY_BE_ELIGIBLE(NAME),
  NOT_ELIGIBLE(),

  // Main reason journey
  WALKING_DIFFICULTY(MAY_BE_ELIGIBLE, NOT_ELIGIBLE),
  CONTACT_COUNCIL(),
  MAIN_REASON(CONTACT_COUNCIL, WALKING_DIFFICULTY, MAY_BE_ELIGIBLE, NOT_ELIGIBLE, ELIGIBLE),

  // DLA Journey
  HIGHER_RATE_MOBILITY(ELIGIBLE, MAIN_REASON),

  // PIP Journey
  PIP_DLA(ELIGIBLE, MAIN_REASON),
  PIP_PLANNING_JOURNEY(PIP_DLA, ELIGIBLE, MAIN_REASON),
  PIP_MOVING_AROUND(PIP_PLANNING_JOURNEY, ELIGIBLE, MAIN_REASON),

  // AFCS Journey
  AFCS_MENTAL_DISORDER(ELIGIBLE, MAIN_REASON),
  AFCS_DISABILITY(ELIGIBLE, MAIN_REASON),
  AFCS_COMPENSATION_SCHEME(MAIN_REASON, AFCS_DISABILITY, AFCS_MENTAL_DISORDER),

  RECEIVE_BENEFITS(
      ELIGIBLE, MAIN_REASON, PIP_MOVING_AROUND, AFCS_COMPENSATION_SCHEME, HIGHER_RATE_MOBILITY),
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
