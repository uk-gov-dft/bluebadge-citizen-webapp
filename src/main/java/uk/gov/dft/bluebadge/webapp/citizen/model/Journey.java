package uk.gov.dft.bluebadge.webapp.citizen.model;

import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.WALKD;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import lombok.extern.slf4j.Slf4j;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.LocalAuthorityRefData;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.Nation;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantType;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.DateOfBirthForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthConditionsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ReceiveBenefitsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.WhereCanYouWalkForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.MainReasonForm;

@Slf4j
public class Journey implements Serializable {

  public static final String JOURNEY_SESSION_KEY = "JOURNEY";
  public static final String FORM_REQUEST = "formRequest";

  private Map<StepDefinition, StepForm> forms = new HashMap<>();
  public String who;
  public String ageGroup;

  public void setFormForStep(StepForm form) {
    // If changing values in a form may need to invalidate later forms in the journey
    if (hasStepForm(form.getAssociatedStep())
        && !form.equals(getFormForStep(form.getAssociatedStep()))) {
      cleanUpSteps(new HashSet<>(), form.getCleanUpSteps(this));
    }
    forms.put(form.getAssociatedStep(), form);

    if (form.getAssociatedStep() == StepDefinition.APPLICANT_TYPE) {
      who = isApplicantYourself() ? "you." : "oth.";
    } else if (form.getAssociatedStep() == StepDefinition.DOB) {
      ageGroup = isApplicantYoung() ? "young." : "adult.";
    }
  }

  @SuppressWarnings("unchecked")
  public <T> T getFormForStep(StepDefinition step) {
    return (T) forms.get(step);
  }

  private void cleanUpSteps(Set<StepDefinition> alreadyCleaned, List<StepDefinition> steps) {

    if (null == steps) {
      return;
    }

    steps
        .stream()
        .filter(((Predicate<StepDefinition>) alreadyCleaned::contains).negate())
        .forEach(
            stepDefinition -> {
              if (hasStepForm(stepDefinition)) {
                StepForm f = getFormForStep(stepDefinition);
                cleanUpSteps(alreadyCleaned, f.getCleanUpSteps(this));
                forms.remove(stepDefinition);
                alreadyCleaned.add(stepDefinition);
              }
            });
  }

  public boolean hasStepForm(StepDefinition stepDefinition) {
    return getFormForStep(stepDefinition) != null;
  }

  private LocalAuthorityRefData localAuthority;

  public Boolean isApplicantYourself() {
    if (hasStepForm(StepDefinition.APPLICANT_TYPE)) {
      ApplicantForm form = getFormForStep(StepDefinition.APPLICANT_TYPE);
      return form.getApplicantType().equals(ApplicantType.YOURSELF.toString());
    }
    return null;
  }

  public Boolean isApplicantYoung() {
    if (hasStepForm(StepDefinition.DOB)) {
      DateOfBirthForm dateOfBirthForm = getFormForStep(StepDefinition.DOB);
      return dateOfBirthForm
          .getDateOfBirth()
          .getLocalDate()
          .isAfter(LocalDate.now().minusYears(16L));
    }
    return null;
  }

  private Map<StepDefinition, List<StepDefinition>> prerequisiteSteps;

  private List<StepDefinition> getPrerequisiteStep(StepDefinition currentStep) {
    if (null == prerequisiteSteps) {
      populatePrerequisiteSteps();
    }
    return prerequisiteSteps.get(currentStep);
  }

  /**
   * Create a list of possible prerequisite steps. This will be used to assume that any step being
   * visited has at least One of the prerequisites completed. Most steps have One possible previous
   * step, so this is fine for validating the journey. Any custom journey validation is done outside
   * of this method.
   */
  private void populatePrerequisiteSteps() {
    prerequisiteSteps = new HashMap<>();
    // Parse twice rather than try and be clever.
    // 1st pass fill in steps and an empty map.
    // 2nd pass add prerequisite steps to map.
    Arrays.stream(StepDefinition.values())
        .forEach(i -> prerequisiteSteps.put(i, new ArrayList<>()));

    for (StepDefinition previousStep : StepDefinition.values()) {
      for (StepDefinition step : previousStep.getNext()) {
        prerequisiteSteps.get(step).add(previousStep);
      }
    }
  }

  public boolean isValidState(StepDefinition step) {
    List<StepDefinition> possiblePreviousSteps = getPrerequisiteStep(step);

    // Custom step validation
    switch (step) {
        // The add pages don't link together in the step definition
        // in the same way as would have created loop with the list
        // pages.  Check the list pages prerequisites
      case MOBILITY_AID_ADD:
        return isValidState(StepDefinition.MOBILITY_AID_LIST);
      case TREATMENT_ADD:
        return isValidState(StepDefinition.TREATMENT_LIST);
      case HEALTHCARE_PROFESSIONALS_ADD:
        return isValidState(StepDefinition.HEALTHCARE_PROFESSIONAL_LIST);
      case MEDICATION_ADD:
        return isValidState(StepDefinition.MEDICATION_LIST);
    }

    // No previous step possible.  Must be home page.
    if (possiblePreviousSteps.isEmpty()) {
      return true;
    }

    // If can only have come from one place.
    if (possiblePreviousSteps.size() == 1) {
      return hasStepForm(possiblePreviousSteps.get(0));
    }

    // More than one place can have come from
    // Replay the journey to find previous step
    StepForm form = getFormForStep(StepDefinition.APPLICANT_TYPE);
    if (null == form) return false;

    StepDefinition previousLoopStep = null;
    StepDefinition currentLoopStep = form.getAssociatedStep();
    while (currentLoopStep != step) {

      StepDefinition nextStep;
      if (currentLoopStep.getNext().size() == 1) {
        nextStep = currentLoopStep.getDefaultNext();
      } else {
        // Is there a break in the journey (should not happen if all steps are validating properly)
        if (!hasStepForm(currentLoopStep)) {
          log.error("Expected step form missing: {}", currentLoopStep);
          return false;
        }

        StepForm currentLoopForm = getFormForStep(currentLoopStep);
        Optional<StepDefinition> possibleNextStep = currentLoopForm.determineNextStep();
        if (!possibleNextStep.isPresent()) {
          possibleNextStep = currentLoopForm.determineNextStep(this);
        }
        if (!possibleNextStep.isPresent()) {
          log.error(
              "Could not determine next step in journey."
                  + "Attempting to check isValidState for {}. Got to {} in journey.",
              step,
              currentLoopStep);
          return false;
        } else {
          nextStep = possibleNextStep.get();
        }
      }
      previousLoopStep = currentLoopStep;
      currentLoopStep = nextStep;
    }

    // previousLoopStep is the form that led to the one we are checking
    return hasStepForm(previousLoopStep);
  }

  // -- META DATA BELOW --
  public LocalAuthorityRefData getLocalAuthority() {
    return localAuthority;
  }

  public void setLocalAuthority(LocalAuthorityRefData localAuthority) {
    this.localAuthority = localAuthority;
  }

  public Nation getNation() {
    if (null != localAuthority) {
      return localAuthority.getNation();
    }
    return null;
  }

  public EligibilityCodeField getEligibilityCode() {
    if (hasStepForm(StepDefinition.MAIN_REASON)) {
      MainReasonForm mainReasonForm = getFormForStep(StepDefinition.MAIN_REASON);
      if (EligibilityCodeField.NONE != mainReasonForm.getMainReasonOption()) {
        return mainReasonForm.getMainReasonOption();
      }
    } else if (hasStepForm(StepDefinition.RECEIVE_BENEFITS)) {
      ReceiveBenefitsForm receiveBenefitsForm = getFormForStep(StepDefinition.RECEIVE_BENEFITS);
      if (EligibilityCodeField.NONE != receiveBenefitsForm.getBenefitType()) {
        return receiveBenefitsForm.getBenefitType();
      }
    }
    return null;
  }

  public String getDescriptionOfCondition() {
    HealthConditionsForm healthConditionsForm = getFormForStep(StepDefinition.HEALTH_CONDITIONS);
    WhereCanYouWalkForm whereCanYouWalkForm = getFormForStep(StepDefinition.WHERE_CAN_YOU_WALK);

    StringBuilder descriptionOfCondition = new StringBuilder();
    if (healthConditionsForm != null && healthConditionsForm.getDescriptionOfConditions() != null) {
      descriptionOfCondition.append(healthConditionsForm.getDescriptionOfConditions());
    }

    if (WALKD.equals(getEligibilityCode()) && whereCanYouWalkForm != null) {
      descriptionOfCondition
          .append(" - Able to walk to: ")
          .append(whereCanYouWalkForm.getDestinationToHome())
          .append(" - How long: ")
          .append(whereCanYouWalkForm.getTimeToDestination());
    }
    if (descriptionOfCondition.length() == 0) {
      descriptionOfCondition.append("Dummy condition");
    }
    return descriptionOfCondition.toString();
  }
}
