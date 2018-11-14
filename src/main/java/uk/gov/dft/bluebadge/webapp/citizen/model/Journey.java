package uk.gov.dft.bluebadge.webapp.citizen.model;

import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.WALKD;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MobilityAidListForm;
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
  public String walkingAid;

  private LocalAuthorityRefData localAuthority;

  public void setFormForStep(StepForm form) {
    // If changing values in a form may need to invalidate later forms in the journey
    boolean doCleanUp =
        (hasStepForm(form.getAssociatedStep())
            && !form.equals(getFormForStep(form.getAssociatedStep())));

    forms.put(form.getAssociatedStep(), form);

    if (form.getAssociatedStep() == StepDefinition.APPLICANT_TYPE) {
      who = isApplicantYourself() ? "you." : "oth.";
    } else if (form.getAssociatedStep() == StepDefinition.DOB) {
      ageGroup = isApplicantYoung() ? "young." : "adult.";
    } else if (form.getAssociatedStep() == StepDefinition.MOBILITY_AID_LIST) {
      walkingAid = hasMobilityAid() ? "withWalkingAid." : "withoutWalkingAid.";
    }

    if (doCleanUp) {
      cleanUpSteps(new HashSet<>(), form.getAssociatedStep().getNext());
    }
  }

  @SuppressWarnings("unchecked")
  public <T> T getFormForStep(StepDefinition step) {
    return (T) forms.get(step);
  }

  private void cleanUpSteps(Set<StepDefinition> alreadyCleaned, Set<StepDefinition> steps) {

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
                cleanUpSteps(alreadyCleaned, f.getAssociatedStep().getNext());
                if (!f.preserveStep(this)) {
                  forms.remove(stepDefinition);
                }
                alreadyCleaned.add(stepDefinition);
              }
            });
  }

  public boolean hasStepForm(StepDefinition stepDefinition) {
    return getFormForStep(stepDefinition) != null;
  }

  public Boolean isApplicantYourself() {
    if (hasStepForm(StepDefinition.APPLICANT_TYPE)) {
      ApplicantForm form = getFormForStep(StepDefinition.APPLICANT_TYPE);
      return form.getApplicantType().equals(ApplicantType.YOURSELF.toString())
          || form.getApplicantType().equals(ApplicantType.ORGANISATION.toString());
    }
    return null;
  }

  public Boolean isApplicantOrganisation() {
    if (hasStepForm(StepDefinition.APPLICANT_TYPE)) {
      ApplicantForm form = getFormForStep(StepDefinition.APPLICANT_TYPE);
      return form.getApplicantType().equals(ApplicantType.ORGANISATION.toString());
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

  private boolean hasMobilityAid() {
    if (hasStepForm(StepDefinition.MOBILITY_AID_LIST)) {
      MobilityAidListForm mobilityAidListForm = getFormForStep(StepDefinition.MOBILITY_AID_LIST);
      return "yes".equals(mobilityAidListForm.getHasWalkingAid());
    }
    return false;
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
