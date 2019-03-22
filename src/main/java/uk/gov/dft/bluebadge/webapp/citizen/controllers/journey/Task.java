package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.*;

import com.google.common.collect.ImmutableList;
import java.util.List;
import lombok.Getter;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

public enum Task {
  CHECK_ELIGIBILITY(
      "Some title here", // TODO
      new CheckEligibilityTask(),
      HOME,
      APPLICANT_TYPE,
      CHOOSE_COUNCIL,
      DIFFERENT_SERVICE_SIGNPOST,
      YOUR_ISSUING_AUTHORITY,
      EXISTING_BADGE,
      RECEIVE_BENEFITS,
      ELIGIBLE,
      MAY_BE_ELIGIBLE,
      NOT_ELIGIBLE,

      // Organisation journey
      ORGANISATION_NOT_ELIGIBLE,
      ORGANISATION_MAY_BE_ELIGIBLE,
      ORGANISATION_TRANSPORT,
      ORGANISATION_CARE,

      // Main reason journey
      WALKING_DIFFICULTY,
      CONTACT_COUNCIL,
      MAIN_REASON,

      // DLA Journey
      HIGHER_RATE_MOBILITY,

      // PIP Journey
      PIP_DLA,
      PIP_PLANNING_JOURNEY,
      PIP_MOVING_AROUND,

      // AFCS Journey
      AFCS_MENTAL_DISORDER,
      AFCS_DISABILITY,
      AFCS_COMPENSATION_SCHEME),
  PERSONAL_DETAILS("Enter personal details", NAME, DOB, GENDER, NINO, ADDRESS, CONTACT_DETAILS),
  PROOF_OF_BENEFIT("Provide proof of benefit", PROVE_BENEFIT, UPLOAD_BENEFIT),
  PROVE_ID("Prove identity", PROVE_IDENTITY),
  PROVE_ADDRESS(
      "Prove address",
      new ProveAddressTask(), // TODO Not applicable if child
      StepDefinition.PROVE_ADDRESS),
  PROVIDE_PHOTO("Add a photo of yourself", StepDefinition.PROVIDE_PHOTO),
  PROOF_OF_VISUAL("Provide proof of visual impairment", REGISTERED, PERMISSION, REGISTERED_COUNCIL),
  DESC_CONDITION_ARMS(
      "Describe your condition",
      HEALTH_CONDITIONS,
      ARMS_HOW_OFTEN_DRIVE,
      ARMS_DRIVE_ADAPTED_VEHICLE,
      ARMS_DIFFICULTY_PARKING_METER),
  DESC_CONDITION_CHILD("Describe your condition", HEALTH_CONDITIONS),
  DESC_WALKING_ABILITY(
      "Describe walking ability",
      HEALTH_CONDITIONS,
      WHAT_MAKES_WALKING_DIFFICULT,
      MOBILITY_AID_LIST,
      WALKING_TIME,
      WHERE_CAN_YOU_WALK),
  SUPPORTING_DOCS("Add supporting documents", UPLOAD_SUPPORTING_DOCUMENTS),
  LIST_MEDICAL_EQUIP("List medical equipment", MEDICAL_EQUIPMENT),
  LIST_HEALTH_PROFS("List healthcare professionals", HEALTHCARE_PROFESSIONAL_LIST),
  LIST_MEDICATION("List medication", MEDICATION_LIST),
  LIST_TREATMENTS("List treatments", TREATMENT_LIST),
  DECLARATION("Read Declaration", DECLARATIONS),
  SUBMIT_AND_PAY("List treatments", BADGE_PAYMENT, NOT_PAID, BADGE_PAYMENT_RETURN);

  @Getter private final String titleCode;
  @Getter private final TaskConfig taskConfig;
  @Getter private final List<StepDefinition> steps;

  Task(String titleCode, TaskConfig taskConfig, StepDefinition... steps) {
    this.titleCode = titleCode;
    this.taskConfig = taskConfig;
    this.steps = ImmutableList.copyOf(steps);
  }

  Task(String titleCode, StepDefinition... steps) {
    this(titleCode, new TaskConfig() {}, steps);
  }

  public StepDefinition getFirstStep(Journey journey) {
    return taskConfig.getFirstStep(journey, steps);
  }

  public StepDefinition getNextStep(Journey journey, StepDefinition current) {
    if (!steps.contains(current)) {
      throw new IllegalArgumentException("Step " + current + " is not part of this task:" + this);
    }
    if (!current.getNext().isEmpty()) {
      // Config error - current step is a deciding step
      throw new IllegalStateException(
          "Cannot determine next step. Current step " + current + ", has multiple exit steps.");
    }
    return taskConfig.getNextStep(journey, steps, current);
  }

  //  public static Task findByStep

  // tests
  // All steps have next steps that are within the task
  // No steps are task-less
  // What determines the next step? The step form or the Task-steps array
  // Step form, then task, then journey def
  // If end of task, then next task. If not more tasks, then next journey def (only until we have task list page)

  // Multiple tasks can have the same step.
  // But any given journey can only have a step once. And a task once.

  // Determine next step - from Form data, from Journey, other

  // Some tasks are dependant on steps answers. Provide address not required for children

  // Proxy session not cleared on final steps
  // Hit back after final step and then no session attribute.
  // Mix and matching the journey session type is not a good idea. Move all to session proxy
  // Session proxy fails to de-serialise on re-boot. Different CGLIB class
}
