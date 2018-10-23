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
import uk.gov.dft.bluebadge.webapp.citizen.model.form.organisation.OrganisationCareForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.organisation.OrganisationTransportForm;
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

  public void setFormForStep(StepDefinition definition, StepForm form) {
    cleanUpSteps(new HashSet<>(), form.getCleanUpSteps(this));
    forms.put(definition, form);
  }

  public StepForm getFormForStep(StepDefinition step) {
    return forms.get(step);
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
      ApplicantForm form = (ApplicantForm) getFormForStep(StepDefinition.APPLICANT_TYPE);
      return form.getApplicantType().equals(ApplicantType.YOURSELF.toString())
          || form.getApplicantType().equals(ApplicantType.ORGANISATION.toString());
    }
    return null;
  }

  public Boolean isApplicantOrganisation() {
    if (hasStepForm(StepDefinition.APPLICANT_TYPE)) {
      ApplicantForm form = (ApplicantForm) getFormForStep(StepDefinition.APPLICANT_TYPE);
      return form.getApplicantType().equals(ApplicantType.ORGANISATION.toString());
    }
    return null;
  }

  public Boolean isApplicantYoung() {
    if (hasStepForm(StepDefinition.DOB)) {
      DateOfBirthForm dateOfBirthForm = (DateOfBirthForm) getFormForStep(StepDefinition.DOB);
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
    setFormForStep(StepDefinition.APPLICANT_TYPE, applicantForm);
    who = isApplicantYourself() ? "you." : "oth.";
  }

  public void setApplicantNameForm(ApplicantNameForm applicantNameForm) {
    setFormForStep(StepDefinition.NAME, applicantNameForm);
  }

  public ApplicantNameForm getApplicantNameForm() {
    return (ApplicantNameForm) getFormForStep(StepDefinition.NAME);
  }

  public void setHealthConditionsForm(HealthConditionsForm healthConditionsForm) {
    setFormForStep(StepDefinition.HEALTH_CONDITIONS, healthConditionsForm);
  }

  public HealthConditionsForm getHealthConditionsForm() {
    return (HealthConditionsForm) getFormForStep(StepDefinition.HEALTH_CONDITIONS);
  }

  public void setDateOfBirthForm(DateOfBirthForm dateOfBirthForm) {
    setFormForStep(StepDefinition.DOB, dateOfBirthForm);
    ageGroup = isApplicantYoung() ? "young." : "adult.";
  }

  public DateOfBirthForm getDateOfBirthForm() {
    return (DateOfBirthForm) getFormForStep(StepDefinition.DOB);
  }

  public ChooseYourCouncilForm getChooseYourCouncilForm() {
    return (ChooseYourCouncilForm) getFormForStep(StepDefinition.CHOOSE_COUNCIL);
  }

  public void setChooseYourCouncilForm(ChooseYourCouncilForm chooseYourCouncilForm) {
    setFormForStep(StepDefinition.CHOOSE_COUNCIL, chooseYourCouncilForm);
  }

  public YourIssuingAuthorityForm getYourIssuingAuthorityForm() {
    return (YourIssuingAuthorityForm) getFormForStep(StepDefinition.YOUR_ISSUING_AUTHORITY);
  }

  public void setYourIssuingAuthorityForm(YourIssuingAuthorityForm yourIssuingAuthorityForm) {
    setFormForStep(StepDefinition.YOUR_ISSUING_AUTHORITY, yourIssuingAuthorityForm);
  }

  public ReceiveBenefitsForm getReceiveBenefitsForm() {
    return (ReceiveBenefitsForm) getFormForStep(StepDefinition.RECEIVE_BENEFITS);
  }

  public void setReceiveBenefitsForm(ReceiveBenefitsForm receiveBenefitsForm) {
    setFormForStep(StepDefinition.RECEIVE_BENEFITS, receiveBenefitsForm);
    // setMainReasonForm(null);
  }

  public PipMovingAroundForm getPipMovingAroundForm() {
    return (PipMovingAroundForm) getFormForStep(StepDefinition.PIP_MOVING_AROUND);
  }

  public void setPipMovingAroundForm(PipMovingAroundForm pipMovingAroundForm) {
    setFormForStep(StepDefinition.PIP_MOVING_AROUND, pipMovingAroundForm);
  }

  public PipDlaQuestionForm getPipDlaQuestionForm() {
    return (PipDlaQuestionForm) getFormForStep(StepDefinition.PIP_DLA);
  }

  public void setPipDlaQuestionForm(PipDlaQuestionForm pipDlaQuestionForm) {
    setFormForStep(StepDefinition.PIP_DLA, pipDlaQuestionForm);
  }

  public PipPlanningJourneyForm getPipPlanningJourneyForm() {
    return (PipPlanningJourneyForm) getFormForStep(StepDefinition.PIP_PLANNING_JOURNEY);
  }

  public void setPipPlanningJourneyForm(PipPlanningJourneyForm pipPlanningJourneyForm) {
    setFormForStep(StepDefinition.PIP_PLANNING_JOURNEY, pipPlanningJourneyForm);
  }

  public CompensationSchemeForm getCompensationSchemeForm() {
    return (CompensationSchemeForm) getFormForStep(StepDefinition.AFCS_COMPENSATION_SCHEME);
  }

  public void setCompensationSchemeForm(CompensationSchemeForm compensationSchemeForm) {
    setFormForStep(StepDefinition.AFCS_COMPENSATION_SCHEME, compensationSchemeForm);
  }

  public DisabilityForm getDisabilityForm() {
    return (DisabilityForm) getFormForStep(StepDefinition.AFCS_DISABILITY);
  }

  public void setDisabilityForm(DisabilityForm disabilityForm) {
    setFormForStep(StepDefinition.AFCS_DISABILITY, disabilityForm);
  }

  public MentalDisorderForm getMentalDisorderForm() {
    return (MentalDisorderForm) getFormForStep(StepDefinition.AFCS_MENTAL_DISORDER);
  }

  public void setMentalDisorderForm(MentalDisorderForm mentalDisorderForm) {
    setFormForStep(StepDefinition.AFCS_MENTAL_DISORDER, mentalDisorderForm);
  }

  public HigherRateMobilityForm getHigherRateMobilityForm() {
    return (HigherRateMobilityForm) getFormForStep(StepDefinition.HIGHER_RATE_MOBILITY);
  }

  public void setHigherRateMobilityForm(HigherRateMobilityForm higherRateMobilityForm) {
    setFormForStep(StepDefinition.HIGHER_RATE_MOBILITY, higherRateMobilityForm);
  }

  public MainReasonForm getMainReasonForm() {
    return (MainReasonForm) getFormForStep(StepDefinition.MAIN_REASON);
  }

  public void setMainReasonForm(MainReasonForm mainReasonForm) {
    setFormForStep(StepDefinition.MAIN_REASON, mainReasonForm);
  }

  public WalkingDifficultyForm getWalkingDifficultyForm() {
    return (WalkingDifficultyForm) getFormForStep(StepDefinition.WALKING_DIFFICULTY);
  }

  public void setWalkingDifficultyForm(WalkingDifficultyForm walkingDifficultyForm) {
    setFormForStep(StepDefinition.WALKING_DIFFICULTY, walkingDifficultyForm);
  }

  public WhereCanYouWalkForm getWhereCanYouWalkForm() {
    return (WhereCanYouWalkForm) getFormForStep(StepDefinition.WHERE_CAN_YOU_WALK);
  }

  public void setWhereCanYouWalkForm(WhereCanYouWalkForm whereCanYouWalkForm) {
    setFormForStep(StepDefinition.WHERE_CAN_YOU_WALK, whereCanYouWalkForm);
  }

  public GenderForm getGenderForm() {
    return (GenderForm) getFormForStep(StepDefinition.GENDER);
  }

  public void setGenderForm(GenderForm genderForm) {
    setFormForStep(StepDefinition.GENDER, genderForm);
  }

  public ContactDetailsForm getContactDetailsForm() {
    return (ContactDetailsForm) getFormForStep(StepDefinition.CONTACT_DETAILS);
  }

  public void setContactDetailsForm(ContactDetailsForm contactDetailsForm) {
    setFormForStep(StepDefinition.CONTACT_DETAILS, contactDetailsForm);
  }

  public NinoForm getNinoForm() {
    return (NinoForm) getFormForStep(StepDefinition.NINO);
  }

  public void setNinoForm(NinoForm ninoForm) {
    setFormForStep(StepDefinition.NINO, ninoForm);
  }

  public EnterAddressForm getEnterAddressForm() {
    return (EnterAddressForm) getFormForStep(StepDefinition.ADDRESS);
  }

  public void setEnterAddressForm(EnterAddressForm enterAddressForm) {
    setFormForStep(StepDefinition.ADDRESS, enterAddressForm);
  }

  public WhatMakesWalkingDifficultForm getWhatMakesWalkingDifficultForm() {
    return (WhatMakesWalkingDifficultForm) getFormForStep(StepDefinition.WHAT_WALKING_DIFFICULTIES);
  }

  public void setWhatMakesWalkingDifficultForm(
      WhatMakesWalkingDifficultForm whatMakesWalkingDifficultForm) {
    setFormForStep(StepDefinition.WHAT_WALKING_DIFFICULTIES, whatMakesWalkingDifficultForm);
  }

  public WalkingTimeForm getWalkingTimeForm() {
    return (WalkingTimeForm) getFormForStep(StepDefinition.WALKING_TIME);
  }

  public void setWalkingTimeForm(WalkingTimeForm walkingTimeForm) {
    setFormForStep(StepDefinition.WALKING_TIME, walkingTimeForm);
  }

  public MobilityAidListForm getMobilityAidListForm() {
    return (MobilityAidListForm) getFormForStep(StepDefinition.MOBILITY_AID_LIST);
  }

  public void setMobilityAidListForm(MobilityAidListForm mobilityAidListForm) {
    setFormForStep(StepDefinition.MOBILITY_AID_LIST, mobilityAidListForm);
  }

  public MedicationListForm getMedicationListForm() {
    return (MedicationListForm) getFormForStep(StepDefinition.MEDICATION_LIST);
  }

  public void setMedicationListForm(MedicationListForm medicationListForm) {
    setFormForStep(StepDefinition.MEDICATION_LIST, medicationListForm);
  }

  public TreatmentListForm getTreatmentListForm() {
    return (TreatmentListForm) getFormForStep(StepDefinition.TREATMENT_LIST);
  }

  public void setTreatmentListForm(TreatmentListForm treatmentListForm) {
    setFormForStep(StepDefinition.TREATMENT_LIST, treatmentListForm);
  }

  public OrganisationCareForm getOrganisationCareForm() {
    return (OrganisationCareForm) getFormForStep(StepDefinition.ORGANISATION_CARE);
  }

  public void setOrganisationCareForm(OrganisationCareForm organisationCareForm) {
    setFormForStep(StepDefinition.ORGANISATION_CARE, organisationCareForm);
  }

  public OrganisationTransportForm getOrganisationTransportForm() {
    return (OrganisationTransportForm) getFormForStep(StepDefinition.ORGANISATION_TRANSPORT);
  }

  public void setOrganisationTransportForm(OrganisationTransportForm organisationTransportForm) {
    setFormForStep(StepDefinition.ORGANISATION_TRANSPORT, organisationTransportForm);
  }

  public HealthcareProfessionalListForm getHealthcareProfessionalListForm() {
    return (HealthcareProfessionalListForm)
        getFormForStep(StepDefinition.HEALTHCARE_PROFESSIONAL_LIST);
  }

  public void setHealthcareProfessionalListForm(
      HealthcareProfessionalListForm healthcareProfessionalListForm) {
    setFormForStep(StepDefinition.HEALTHCARE_PROFESSIONAL_LIST, healthcareProfessionalListForm);
  }

  public ExistingBadgeForm getExistingBadgeForm() {
    return (ExistingBadgeForm) getFormForStep(StepDefinition.EXISTING_BADGE);
  }

  public void setExistingBadgeForm(ExistingBadgeForm existingBadgeForm) {
    setFormForStep(StepDefinition.EXISTING_BADGE, existingBadgeForm);
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
      MainReasonForm mainReasonForm = (MainReasonForm) getFormForStep(StepDefinition.MAIN_REASON);
      if (EligibilityCodeField.NONE != mainReasonForm.getMainReasonOption()) {
        return mainReasonForm.getMainReasonOption();
      }
    } else if (hasStepForm(StepDefinition.RECEIVE_BENEFITS)) {
      ReceiveBenefitsForm receiveBenefitsForm =
          (ReceiveBenefitsForm) getFormForStep(StepDefinition.RECEIVE_BENEFITS);
      if (EligibilityCodeField.NONE != receiveBenefitsForm.getBenefitType()) {
        return receiveBenefitsForm.getBenefitType();
      }
    }
    return null;
  }

  public String getDescriptionOfCondition() {
    HealthConditionsForm healthConditionsForm = getHealthConditionsForm();
    WhereCanYouWalkForm whereCanYouWalkForm = getWhereCanYouWalkForm();

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
