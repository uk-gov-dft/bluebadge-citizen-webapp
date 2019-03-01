package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum StepDefinition {
  SUBMITTED(),
  NOT_PAID(SUBMITTED),
  BADGE_PAYMENT_RETURN(SUBMITTED, NOT_PAID),
  BADGE_PAYMENT(SUBMITTED),
  DECLARATIONS(BADGE_PAYMENT, SUBMITTED),
  PROVE_ADDRESS(DECLARATIONS),
  PROVIDE_PHOTO(PROVE_ADDRESS, DECLARATIONS),
  PROVE_IDENTITY(PROVIDE_PHOTO),
  REGISTERED_COUNCIL(PROVE_IDENTITY),
  PERMISSION(REGISTERED_COUNCIL, PROVE_IDENTITY),
  REGISTERED(PERMISSION, PROVE_IDENTITY),
  HEALTHCARE_PROFESSIONAL_LIST(PROVE_IDENTITY),
  HEALTHCARE_PROFESSIONALS_ADD(HEALTHCARE_PROFESSIONAL_LIST),
  MEDICAL_EQUIPMENT(HEALTHCARE_PROFESSIONAL_LIST),
  MEDICATION_LIST(HEALTHCARE_PROFESSIONAL_LIST),
  MEDICATION_ADD(MEDICATION_LIST),
  TREATMENT_LIST(MEDICATION_LIST),
  TREATMENT_ADD(TREATMENT_LIST),

  ARMS_DIFFICULTY_PARKING_METER(PROVE_IDENTITY),
  ARMS_DRIVE_ADAPTED_VEHICLE(ARMS_DIFFICULTY_PARKING_METER),
  ARMS_HOW_OFTEN_DRIVE(ARMS_DRIVE_ADAPTED_VEHICLE),

  UPLOAD_SUPPORTING_DOCUMENTS(
      TREATMENT_LIST, ARMS_HOW_OFTEN_DRIVE, MEDICAL_EQUIPMENT, HEALTHCARE_PROFESSIONAL_LIST),
  WHERE_CAN_YOU_WALK(UPLOAD_SUPPORTING_DOCUMENTS),
  WALKING_TIME(WHERE_CAN_YOU_WALK, UPLOAD_SUPPORTING_DOCUMENTS),
  MOBILITY_AID_LIST(WALKING_TIME),
  MOBILITY_AID_ADD(MOBILITY_AID_LIST),
  WHAT_MAKES_WALKING_DIFFICULT(MOBILITY_AID_LIST),
  HEALTH_CONDITIONS(WHAT_MAKES_WALKING_DIFFICULT, PROVE_IDENTITY, UPLOAD_SUPPORTING_DOCUMENTS),
  UPLOAD_BENEFIT(PROVE_IDENTITY),
  PROVE_BENEFIT(UPLOAD_BENEFIT),
  CONTACT_DETAILS(PROVE_BENEFIT, HEALTH_CONDITIONS, PROVE_IDENTITY, REGISTERED),
  ADDRESS(CONTACT_DETAILS),
  NINO(ADDRESS),
  GENDER(NINO, ADDRESS),
  DOB(GENDER),
  NAME(DOB),
  ELIGIBLE(NAME),
  MAY_BE_ELIGIBLE(NAME),
  NOT_ELIGIBLE(),

  // Organisation journey
  ORGANISATION_NOT_ELIGIBLE(),
  ORGANISATION_MAY_BE_ELIGIBLE(),
  ORGANISATION_TRANSPORT(ORGANISATION_MAY_BE_ELIGIBLE, ORGANISATION_NOT_ELIGIBLE),
  ORGANISATION_CARE(ORGANISATION_TRANSPORT, ORGANISATION_NOT_ELIGIBLE),

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
  EXISTING_BADGE(RECEIVE_BENEFITS),
  YOUR_ISSUING_AUTHORITY(EXISTING_BADGE, ORGANISATION_CARE),
  DIFFERENT_SERVICE_SIGNPOST(),
  CHOOSE_COUNCIL(YOUR_ISSUING_AUTHORITY, DIFFERENT_SERVICE_SIGNPOST),
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

  public static StepDefinition getFirstStep() {
    return StepDefinition.APPLICANT_TYPE;
  }
}
