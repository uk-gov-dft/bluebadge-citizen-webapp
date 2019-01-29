package uk.gov.dft.bluebadge.webapp.citizen.model;

import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.ARMS;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.BLIND;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.CHILDBULK;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.CHILDVEHIC;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.DLA;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.PIP;
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
import uk.gov.dft.bluebadge.webapp.citizen.model.form.arms.ArmsDifficultyParkingMetersForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.MainReasonForm;

@Slf4j
public class Journey implements Serializable {

  public static final String JOURNEY_SESSION_KEY = "JOURNEY";
  public static final String FORM_REQUEST = "formRequest";
  public static final String SEPARATOR = "\n- - - - - - - - - - - - - - - - -\n";

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

  public synchronized <T extends StepForm> T getOrSetFormForStep(T form) {
    T formForStep = getFormForStep(form.getAssociatedStep());
    if (formForStep == null) {
      setFormForStep(form);
      formForStep = form;
    }
    return formForStep;
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

  public Boolean isLocalAuthorityActive() {
    return getLocalAuthority()
        .getLocalAuthorityMetaData()
        .map(LocalAuthorityRefData.LocalAuthorityMetaData::getDifferentServiceSignpostUrl)
        .map(String::trim)
        .map(String::isEmpty)
        .orElse(true);
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

  public LocaleAwareRefData<LocalAuthorityRefData> getLocaleAwareLocalAuthority() {
    return new LocaleAwareRefData<>(getLocalAuthority());
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

  public boolean isNationWales() {
    return Nation.WLS == getNation();
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

  // Eligibility codes
  public boolean isEligibilityCodePIP() {
    return PIP == getEligibilityCode();
  }

  public boolean isEligibilityCodeDLA() {
    return DLA == getEligibilityCode();
  }

  public boolean isEligibilityCodeBLIND() {
    return BLIND == getEligibilityCode();
  }

  public boolean isEligibilityCodeCHILDBULK() {
    return CHILDBULK == getEligibilityCode();
  }

  public boolean isEligibilityCodeCHILDVEHIC() {
    return CHILDVEHIC == getEligibilityCode();
  }

  public boolean isRelatedDocumentProofNeeded() {
    switch (getEligibilityCode()) {
      case WALKD:
      case ARMS:
      case CHILDBULK:
      case CHILDVEHIC:
        return true;
      default:
        return false;
    }
  }

  public boolean isAutomaticEligibilityType() {
    switch (getEligibilityCode()) {
      case PIP:
      case DLA:
      case WPMS:
      case AFRFCS:
      case BLIND:
        return true;
      default:
        return false;
    }
  }

  /**
   * Returns a built string whilst this data is not currently stored is separate fields.
   *
   * @return String aggregated string of conditions
   */
  public String getDescriptionOfCondition() {

    StringBuilder descriptionOfCondition = new StringBuilder();

    boolean hasWalkingEligibilityText =
        (WALKD == getEligibilityCode()) && hasStepForm(StepDefinition.WHERE_CAN_YOU_WALK);
    boolean hasArmsEligibilityText =
        (ARMS == getEligibilityCode()) && hasStepForm(StepDefinition.ARMS_DIFFICULTY_PARKING_METER);

    HealthConditionsForm healthConditionsForm = getFormForStep(StepDefinition.HEALTH_CONDITIONS);
    if (healthConditionsForm != null && healthConditionsForm.getDescriptionOfConditions() != null) {

      if (hasWalkingEligibilityText || hasArmsEligibilityText) {
        descriptionOfCondition.append("Description of conditions:\n");
      }

      descriptionOfCondition.append(healthConditionsForm.getDescriptionOfConditions());
    }

    if (hasWalkingEligibilityText) {
      WhereCanYouWalkForm whereCanYouWalkForm = getFormForStep(StepDefinition.WHERE_CAN_YOU_WALK);
      descriptionOfCondition
          .append(SEPARATOR)
          .append("Able to walk to and from:\n")
          .append(whereCanYouWalkForm.getDestinationToHome())
          .append(SEPARATOR)
          .append("How long it takes:\n")
          .append(whereCanYouWalkForm.getTimeToDestination());
    }

    if (hasArmsEligibilityText) {
      ArmsDifficultyParkingMetersForm difficultyParkingMetersForm =
          getFormForStep(StepDefinition.ARMS_DIFFICULTY_PARKING_METER);

      descriptionOfCondition
          .append(SEPARATOR)
          .append("Description of difficulties using a parking meter:\n")
          .append(difficultyParkingMetersForm.getParkingMetersDifficultyDescription());
    }

    return descriptionOfCondition.toString();
  }
}
