package uk.gov.dft.bluebadge.webapp.citizen.model;

import java.io.Serializable;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.LocalAuthorityRefData;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.AFCS.CompensationSchemeForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.AFCS.DisabilityForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.AFCS.MentalDisorderForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantNameForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantType;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ChooseYourCouncilForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.DateOfBirthForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthConditionsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.PipDlaQuestionForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.PipMovingAroundForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.PipPlanningJourneyForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ReceiveBenefitsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.YourIssuingAuthorityForm;

public class Journey implements Serializable {

  public static final String JOURNEY_SESSION_KEY = "JOURNEY";

  private ApplicantForm applicantForm;
  private ApplicantNameForm applicantNameForm;
  private HealthConditionsForm healthConditionsForm;
  private ReceiveBenefitsForm receiveBenefitsForm;;
  private ChooseYourCouncilForm chooseYourCouncilForm;
  private YourIssuingAuthorityForm yourIssuingAuthorityForm;
  private PipMovingAroundForm pipMovingAroundForm;
  private PipDlaQuestionForm pipDlaQuestionForm;
  private PipPlanningJourneyForm pipPlanningJourneyForm;
  private LocalAuthorityRefData localAuthority;
  private DateOfBirthForm dateOfBirthForm;
  public String who;

  // AFCS Journey Forms
  private CompensationSchemeForm compensationSchemeForm;
  private DisabilityForm disabilityForm;
  private MentalDisorderForm mentalDisorderForm;

  public Boolean isApplicantYourself() {
    if (applicantForm != null) {
      return applicantForm.getApplicantType().equals(ApplicantType.YOURSELF.toString());
    }
    return null;
  }

  public String applicantContextContent(String messageKey) {
    if (isApplicantYourself()) {
      return messageKey;
    }

    return "someone." + messageKey;
  }

  public boolean isValidState(StepDefinition step) {
    if (null == getApplicantForm()) {
      return false;
    }

    switch (step) {
      case ELIGIBLE:
      case MAY_BE_ELIGIBLE:
        if (null == getYourIssuingAuthorityForm()) {
          return false;
        }
    }

    return true;
  }

  public ApplicantForm getApplicantForm() {
    return applicantForm;
  }

  public void setApplicantForm(ApplicantForm applicantForm) {
    this.applicantForm = applicantForm;
    who = isApplicantYourself() ? "you." : "oth.";
  }

  public ApplicantNameForm getApplicantNameForm() {
    return applicantNameForm;
  }

  public void setApplicantNameForm(ApplicantNameForm applicantNameForm) {
    this.applicantNameForm = applicantNameForm;
  }

  public HealthConditionsForm getHealthConditionsForm() {
    return healthConditionsForm;
  }

  public void setHealthConditionsForm(HealthConditionsForm healthConditionsForm) {
    this.healthConditionsForm = healthConditionsForm;
  }

  public void setDateOfBirthForm(DateOfBirthForm dateOfBirthForm) {
    this.dateOfBirthForm = dateOfBirthForm;
  }

  public DateOfBirthForm getDateOfBirthForm() {
    return dateOfBirthForm;
  }

  public ChooseYourCouncilForm getChooseYourCouncilForm() {
    return chooseYourCouncilForm;
  }

  public void setChooseYourCouncilForm(ChooseYourCouncilForm chooseYourCouncilForm) {
    this.chooseYourCouncilForm = chooseYourCouncilForm;
  }

  public YourIssuingAuthorityForm getYourIssuingAuthorityForm() {
    return yourIssuingAuthorityForm;
  }

  public void setYourIssuingAuthorityForm(YourIssuingAuthorityForm yourIssuingAuthorityForm) {
    this.yourIssuingAuthorityForm = yourIssuingAuthorityForm;
  }

  public ReceiveBenefitsForm getReceiveBenefitsForm() {
    return receiveBenefitsForm;
  }

  public void setReceiveBenefitsForm(ReceiveBenefitsForm receiveBenefitsForm) {
    this.receiveBenefitsForm = receiveBenefitsForm;
  }

  public PipMovingAroundForm getPipMovingAroundForm() {
    return pipMovingAroundForm;
  }

  public void setPipMovingAroundForm(PipMovingAroundForm pipMovingAroundForm) {
    this.pipMovingAroundForm = pipMovingAroundForm;
  }

  public PipDlaQuestionForm getPipDlaQuestionForm() {
    return pipDlaQuestionForm;
  }

  public void setPipDlaQuestionForm(PipDlaQuestionForm pipDlaQuestionForm) {
    this.pipDlaQuestionForm = pipDlaQuestionForm;
  }

  public PipPlanningJourneyForm getPipPlanningJourneyForm() {
    return pipPlanningJourneyForm;
  }

  public void setPipPlanningJourneyForm(PipPlanningJourneyForm pipPlanningJourneyForm) {
    this.pipPlanningJourneyForm = pipPlanningJourneyForm;
  }

  public LocalAuthorityRefData getLocalAuthority() {
    return localAuthority;
  }

  public void setLocalAuthority(LocalAuthorityRefData localAuthority) {
    this.localAuthority = localAuthority;
  }

  public CompensationSchemeForm getCompensationSchemeForm() {
    return compensationSchemeForm;
  }

  public void setCompensationSchemeForm(CompensationSchemeForm compensationSchemeForm) {
    this.compensationSchemeForm = compensationSchemeForm;
  }

  public DisabilityForm getDisabilityForm() {
    return disabilityForm;
  }

  public void setDisabilityForm(DisabilityForm disabilityForm) {
    this.disabilityForm = disabilityForm;
  }

  public MentalDisorderForm getMentalDisorderForm() {
    return mentalDisorderForm;
  }

  public void setMentalDisorderForm(MentalDisorderForm mentalDisorderForm) {
    this.mentalDisorderForm = mentalDisorderForm;
  }
}
