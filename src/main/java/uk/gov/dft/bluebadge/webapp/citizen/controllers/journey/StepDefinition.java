package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum StepDefinition {
  TASK_LIST(),

  SUBMITTED(), // Terminatory step
  NOT_PAID(SUBMITTED),
  BADGE_PAYMENT_RETURN(SUBMITTED, NOT_PAID),
  BADGE_PAYMENT(SUBMITTED),
  DECLARATIONS(),

  PROVE_ADDRESS(), // Child doesn't need address
  PROVIDE_PHOTO(),
  PROVE_IDENTITY(),

  REGISTERED_COUNCIL(),
  PERMISSION(REGISTERED_COUNCIL), // give permission
  REGISTERED(PERMISSION), // is registered

  HEALTHCARE_PROFESSIONAL_LIST(),
  HEALTHCARE_PROFESSIONALS_ADD(HEALTHCARE_PROFESSIONAL_LIST),

  MEDICAL_EQUIPMENT(),

  MEDICATION_LIST(),
  MEDICATION_ADD(MEDICATION_LIST),

  TREATMENT_LIST(),
  TREATMENT_ADD(TREATMENT_LIST),

  UPLOAD_SUPPORTING_DOCUMENTS(),

  ARMS_DIFFICULTY_PARKING_METER(),
  ARMS_DRIVE_ADAPTED_VEHICLE(),
  ARMS_HOW_OFTEN_DRIVE(),

  WHERE_CAN_YOU_WALK(),
  WALKING_TIME(),
  MOBILITY_AID_LIST(),
  MOBILITY_AID_ADD(MOBILITY_AID_LIST),
  BREATHLESSNESS(),
  WHAT_MAKES_WALKING_DIFFICULT(),

  HEALTH_CONDITIONS(),

  UPLOAD_BENEFIT(),
  PROVE_BENEFIT(),

  CONTACT_DETAILS(),
  ADDRESS(),
  NINO(),
  GENDER(ADDRESS),
  DOB(),
  NAME(),

  ELIGIBLE(),
  MAY_BE_ELIGIBLE(),
  NOT_ELIGIBLE(), // Terminatory step

  // Organisation journey
  ORGANISATION_NOT_ELIGIBLE(), // Terminatory step
  ORGANISATION_MAY_BE_ELIGIBLE(), // Terminatory step
  ORGANISATION_TRANSPORT(ORGANISATION_MAY_BE_ELIGIBLE, ORGANISATION_NOT_ELIGIBLE),
  ORGANISATION_CARE(ORGANISATION_TRANSPORT, ORGANISATION_NOT_ELIGIBLE),

  // Main reason journey
  WALKING_DIFFICULTY(MAY_BE_ELIGIBLE, NOT_ELIGIBLE),
  CONTACT_COUNCIL(), // Terminatory step
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
  EXISTING_BADGE(),
  YOUR_ISSUING_AUTHORITY(),
  DIFFERENT_SERVICE_SIGNPOST(),
  CHOOSE_COUNCIL(YOUR_ISSUING_AUTHORITY, DIFFERENT_SERVICE_SIGNPOST),
  FIND_COUNCIL(CHOOSE_COUNCIL, YOUR_ISSUING_AUTHORITY, DIFFERENT_SERVICE_SIGNPOST),
  APPLICANT_TYPE(),
  HOME(APPLICANT_TYPE);

  private Set<StepDefinition> next;

  // To handle problem of illegal forward references
  static {
    YOUR_ISSUING_AUTHORITY.next =
        ImmutableSet.of(EXISTING_BADGE, ORGANISATION_CARE, CHOOSE_COUNCIL);
    DIFFERENT_SERVICE_SIGNPOST.next = ImmutableSet.of(CHOOSE_COUNCIL);
  }

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
