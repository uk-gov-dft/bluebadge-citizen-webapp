package uk.gov.dft.bluebadge.webapp.citizen.model;

import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.WALKD;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.LocalAuthorityRefData;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.Nation;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantNameForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantType;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ChooseYourCouncilForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ContactDetailsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.DateOfBirthForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.EnterAddressForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ExistingBadgeForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.GenderForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthConditionsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthcareProfessionalListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HigherRateMobilityForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MobilityAidListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.NinoForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ReceiveBenefitsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.TreatmentListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.WhereCanYouWalkForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.YourIssuingAuthorityForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.afcs.CompensationSchemeForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.afcs.DisabilityForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.afcs.MentalDisorderForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.MainReasonForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.WalkingDifficultyForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.pip.PipDlaQuestionForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.pip.PipMovingAroundForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.pip.PipPlanningJourneyForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.MedicationListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.WalkingTimeForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.WhatMakesWalkingDifficultForm;

public class Journey implements Serializable {

  public static final String JOURNEY_SESSION_KEY = "JOURNEY";
  public static final String FORM_REQUEST = "formRequest";

  private Map<StepDefinition, StepForm> forms = new HashMap<>();
  public String who;
  public String ageGroup;

  public void setFormForStep(StepForm form) {
    cleanUpSteps(new HashSet<>(), form.getCleanUpSteps(this));
    forms.put(form.getAssociatedStep(), form);
  }

  @SuppressWarnings("unchecked")
  public <T> T getFormForStep(StepDefinition step) {
    return (T) forms.get(step);
  }

  public void cleanUpSteps(Set<StepDefinition> alreadyCleaned, List<StepDefinition> steps) {

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

  public boolean isValidState(StepDefinition step) {
    if (!hasStepForm(StepDefinition.APPLICANT_TYPE)) {
      return false;
    }

    switch (step) {
      case WHAT_WALKING_DIFFICULTIES:
        if (null == getNation()) {
          return false;
        }
      case ELIGIBLE:
      case MAY_BE_ELIGIBLE:
        if (null == getLocalAuthority()) {
          return false;
        }
    }

    return true;
  }

  public void setApplicantForm(ApplicantForm applicantForm) {
    setFormForStep(applicantForm);
    who = isApplicantYourself() ? "you." : "oth.";
  }

  public void setApplicantNameForm(ApplicantNameForm applicantNameForm) {
    setFormForStep(applicantNameForm);
  }

  public ApplicantNameForm getApplicantNameForm() {
    return getFormForStep(StepDefinition.NAME);
  }

  public void setHealthConditionsForm(HealthConditionsForm healthConditionsForm) {
    setFormForStep(healthConditionsForm);
  }

  public HealthConditionsForm getHealthConditionsForm() {
    return getFormForStep(StepDefinition.HEALTH_CONDITIONS);
  }

  public void setDateOfBirthForm(DateOfBirthForm dateOfBirthForm) {
    setFormForStep(dateOfBirthForm);
    ageGroup = isApplicantYoung() ? "young." : "adult.";
  }

  public DateOfBirthForm getDateOfBirthForm() {
    return (DateOfBirthForm) getFormForStep(StepDefinition.DOB);
  }

  public ChooseYourCouncilForm getChooseYourCouncilForm() {
    return getFormForStep(StepDefinition.CHOOSE_COUNCIL);
  }

  public void setChooseYourCouncilForm(ChooseYourCouncilForm chooseYourCouncilForm) {
    setFormForStep(chooseYourCouncilForm);
  }

  public YourIssuingAuthorityForm getYourIssuingAuthorityForm() {
    return getFormForStep(StepDefinition.YOUR_ISSUING_AUTHORITY);
  }

  public void setYourIssuingAuthorityForm(YourIssuingAuthorityForm yourIssuingAuthorityForm) {
    setFormForStep(yourIssuingAuthorityForm);
  }

  public ReceiveBenefitsForm getReceiveBenefitsForm() {
    return getFormForStep(StepDefinition.RECEIVE_BENEFITS);
  }

  public void setReceiveBenefitsForm(ReceiveBenefitsForm receiveBenefitsForm) {
    setFormForStep(receiveBenefitsForm);
  }

  public PipMovingAroundForm getPipMovingAroundForm() {
    return getFormForStep(StepDefinition.PIP_MOVING_AROUND);
  }

  public void setPipMovingAroundForm(PipMovingAroundForm pipMovingAroundForm) {
    setFormForStep(pipMovingAroundForm);
  }

  public PipDlaQuestionForm getPipDlaQuestionForm() {
    return getFormForStep(StepDefinition.PIP_DLA);
  }

  public void setPipDlaQuestionForm(PipDlaQuestionForm pipDlaQuestionForm) {
    setFormForStep(pipDlaQuestionForm);
  }

  public PipPlanningJourneyForm getPipPlanningJourneyForm() {
    return getFormForStep(StepDefinition.PIP_PLANNING_JOURNEY);
  }

  public void setPipPlanningJourneyForm(PipPlanningJourneyForm pipPlanningJourneyForm) {
    setFormForStep(pipPlanningJourneyForm);
  }

  public CompensationSchemeForm getCompensationSchemeForm() {
    return getFormForStep(StepDefinition.AFCS_COMPENSATION_SCHEME);
  }

  public void setCompensationSchemeForm(CompensationSchemeForm compensationSchemeForm) {
    setFormForStep(compensationSchemeForm);
  }

  public DisabilityForm getDisabilityForm() {
    return getFormForStep(StepDefinition.AFCS_DISABILITY);
  }

  public void setDisabilityForm(DisabilityForm disabilityForm) {
    setFormForStep(disabilityForm);
  }

  public MentalDisorderForm getMentalDisorderForm() {
    return getFormForStep(StepDefinition.AFCS_MENTAL_DISORDER);
  }

  public void setMentalDisorderForm(MentalDisorderForm mentalDisorderForm) {
    setFormForStep(mentalDisorderForm);
  }

  public HigherRateMobilityForm getHigherRateMobilityForm() {
    return getFormForStep(StepDefinition.HIGHER_RATE_MOBILITY);
  }

  public void setHigherRateMobilityForm(HigherRateMobilityForm higherRateMobilityForm) {
    setFormForStep(higherRateMobilityForm);
  }

  public MainReasonForm getMainReasonForm() {
    return getFormForStep(StepDefinition.MAIN_REASON);
  }

  public void setMainReasonForm(MainReasonForm mainReasonForm) {
    setFormForStep(mainReasonForm);
  }

  public WalkingDifficultyForm getWalkingDifficultyForm() {
    return getFormForStep(StepDefinition.WALKING_DIFFICULTY);
  }

  public void setWalkingDifficultyForm(WalkingDifficultyForm walkingDifficultyForm) {
    setFormForStep(walkingDifficultyForm);
  }

  public WhereCanYouWalkForm getWhereCanYouWalkForm() {
    return getFormForStep(StepDefinition.WHERE_CAN_YOU_WALK);
  }

  public void setWhereCanYouWalkForm(WhereCanYouWalkForm whereCanYouWalkForm) {
    setFormForStep(whereCanYouWalkForm);
  }

  public GenderForm getGenderForm() {
    return getFormForStep(StepDefinition.GENDER);
  }

  public void setGenderForm(GenderForm genderForm) {
    setFormForStep(genderForm);
  }

  public ContactDetailsForm getContactDetailsForm() {
    return getFormForStep(StepDefinition.CONTACT_DETAILS);
  }

  public void setContactDetailsForm(ContactDetailsForm contactDetailsForm) {
    setFormForStep(contactDetailsForm);
  }

  public NinoForm getNinoForm() {
    return getFormForStep(StepDefinition.NINO);
  }

  public void setNinoForm(NinoForm ninoForm) {
    setFormForStep(ninoForm);
  }

  public EnterAddressForm getEnterAddressForm() {
    return getFormForStep(StepDefinition.ADDRESS);
  }

  public void setEnterAddressForm(EnterAddressForm enterAddressForm) {
    setFormForStep(enterAddressForm);
  }

  public WhatMakesWalkingDifficultForm getWhatMakesWalkingDifficultForm() {
    return getFormForStep(StepDefinition.WHAT_WALKING_DIFFICULTIES);
  }

  public void setWhatMakesWalkingDifficultForm(
      WhatMakesWalkingDifficultForm whatMakesWalkingDifficultForm) {
    setFormForStep(whatMakesWalkingDifficultForm);
  }

  public WalkingTimeForm getWalkingTimeForm() {
    return getFormForStep(StepDefinition.WALKING_TIME);
  }

  public void setWalkingTimeForm(WalkingTimeForm walkingTimeForm) {
    setFormForStep(walkingTimeForm);
  }

  public MobilityAidListForm getMobilityAidListForm() {
    return getFormForStep(StepDefinition.MOBILITY_AID_LIST);
  }

  public void setMobilityAidListForm(MobilityAidListForm mobilityAidListForm) {
    setFormForStep(mobilityAidListForm);
  }

  public MedicationListForm getMedicationListForm() {
    return getFormForStep(StepDefinition.MEDICATION_LIST);
  }

  public void setMedicationListForm(MedicationListForm medicationListForm) {
    setFormForStep(medicationListForm);
  }

  public TreatmentListForm getTreatmentListForm() {
    return getFormForStep(StepDefinition.TREATMENT_LIST);
  }

  public void setTreatmentListForm(TreatmentListForm treatmentListForm) {
    setFormForStep(treatmentListForm);
  }

  public HealthcareProfessionalListForm getHealthcareProfessionalListForm() {
    return (HealthcareProfessionalListForm)
        getFormForStep(StepDefinition.HEALTHCARE_PROFESSIONAL_LIST);
  }

  public void setHealthcareProfessionalListForm(
      HealthcareProfessionalListForm healthcareProfessionalListForm) {
    setFormForStep(healthcareProfessionalListForm);
  }

  public ExistingBadgeForm getExistingBadgeForm() {
    return (ExistingBadgeForm) getFormForStep(StepDefinition.EXISTING_BADGE);
  }

  public void setExistingBadgeForm(ExistingBadgeForm existingBadgeForm) {
    setFormForStep(existingBadgeForm);
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
