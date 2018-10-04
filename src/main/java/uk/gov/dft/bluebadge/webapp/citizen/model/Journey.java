package uk.gov.dft.bluebadge.webapp.citizen.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
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
import uk.gov.dft.bluebadge.webapp.citizen.model.form.GenderForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthConditionsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HigherRateMobilityForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.NinoForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ReceiveBenefitsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.YourIssuingAuthorityForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.afcs.CompensationSchemeForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.afcs.DisabilityForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.afcs.MentalDisorderForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.MainReasonForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason.WalkingDifficultyForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.pip.PipDlaQuestionForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.pip.PipMovingAroundForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.pip.PipPlanningJourneyForm;

public class Journey implements Serializable {

  public static final String JOURNEY_SESSION_KEY = "JOURNEY";
  public static final String FORM_REQUEST = "formRequest";

  private Map<StepDefinition, StepForm> forms = new HashMap<>();

  public void setFormForStep(StepDefinition definition, StepForm form) {
    cleanUpSteps(form.getCleanUpSteps(this));
    forms.put(definition, form);
  }

  public StepForm getFormForStep(StepDefinition step) {
    return forms.get(step);
  }

  public void cleanUpSteps(StepDefinition... steps) {

    Stream<StepDefinition> stream = Arrays.stream(steps);
    stream.forEach(
        stepDefinition -> {
          StepForm f = getFormForStep(stepDefinition);
          cleanUpSteps(f.getCleanUpSteps(this));
          forms.remove(stepDefinition);
        });
  }

  public boolean hasStepForm(StepDefinition stepDefinition) {
    return getFormForStep(stepDefinition) != null;
  }

  private HealthConditionsForm healthConditionsForm;
  private ReceiveBenefitsForm receiveBenefitsForm;
  private ChooseYourCouncilForm chooseYourCouncilForm;
  private YourIssuingAuthorityForm yourIssuingAuthorityForm;
  private PipMovingAroundForm pipMovingAroundForm;
  private PipDlaQuestionForm pipDlaQuestionForm;
  private PipPlanningJourneyForm pipPlanningJourneyForm;
  private LocalAuthorityRefData localAuthority;
  private DateOfBirthForm dateOfBirthForm;
  private EnterAddressForm enterAddressForm;
  private HigherRateMobilityForm higherRateMobilityForm;
  private GenderForm genderForm;
  private NinoForm ninoForm;
  private MainReasonForm mainReasonForm;
  private WalkingDifficultyForm walkingDifficultyForm;
  public String who;
  public String ageGroup;

  // afcs Journey Forms
  private CompensationSchemeForm compensationSchemeForm;
  private DisabilityForm disabilityForm;
  private MentalDisorderForm mentalDisorderForm;
  private ContactDetailsForm contactDetailsForm;

  public Nation getNation() {
    if (null != localAuthority) {
      return localAuthority.getNation();
    }
    return null;
  }

  public EligibilityCodeField getEligibilityCode() {
    if (null != mainReasonForm
        && EligibilityCodeField.NONE != mainReasonForm.getMainReasonOption()) {
      return mainReasonForm.getMainReasonOption();
    } else if (null != receiveBenefitsForm
        && EligibilityCodeField.NONE != receiveBenefitsForm.getBenefitType()) {
      return receiveBenefitsForm.getBenefitType();
    }

    return null;
  }

  public Boolean isApplicantYourself() {
    if (hasStepForm(StepDefinition.APPLICANT_TYPE)) {
      ApplicantForm form = (ApplicantForm) getFormForStep(StepDefinition.APPLICANT_TYPE);
      return form.getApplicantType().equals(ApplicantType.YOURSELF.toString());
    }
    return null;
  }

  public Boolean isApplicantYoung() {
    if (dateOfBirthForm != null) {
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
//    this.yourIssuingAuthorityForm = yourIssuingAuthorityForm;
  }

  public ReceiveBenefitsForm getReceiveBenefitsForm() {
    return (ReceiveBenefitsForm) getFormForStep(StepDefinition.RECEIVE_BENEFITS);
//    return receiveBenefitsForm;
  }

  public void setReceiveBenefitsForm(ReceiveBenefitsForm receiveBenefitsForm) {
    setFormForStep(StepDefinition.RECEIVE_BENEFITS, receiveBenefitsForm);
//    this.receiveBenefitsForm = receiveBenefitsForm;
    setMainReasonForm(null);
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

  public HigherRateMobilityForm getHigherRateMobilityForm() {
    return higherRateMobilityForm;
  }

  public void setHigherRateMobilityForm(HigherRateMobilityForm higherRateMobilityForm) {
    this.higherRateMobilityForm = higherRateMobilityForm;
  }

  public MainReasonForm getMainReasonForm() {
    return mainReasonForm;
  }

  public void setMainReasonForm(MainReasonForm mainReasonForm) {
    this.mainReasonForm = mainReasonForm;
  }

  public WalkingDifficultyForm getWalkingDifficultyForm() {
    return walkingDifficultyForm;
  }

  public void setWalkingDifficultyForm(WalkingDifficultyForm walkingDifficultyForm) {
    this.walkingDifficultyForm = walkingDifficultyForm;
  }

  public GenderForm getGenderForm() {
    return genderForm;
  }

  public void setGenderForm(GenderForm genderForm) {
    this.genderForm = genderForm;
  }

  public ContactDetailsForm getContactDetailsForm() {
    return contactDetailsForm;
  }

  public void setContactDetailsForm(ContactDetailsForm contactDetailsForm) {
    this.contactDetailsForm = contactDetailsForm;
  }

  public NinoForm getNinoForm() {
    return ninoForm;
  }

  public void setNinoForm(NinoForm ninoForm) {
    this.ninoForm = ninoForm;
  }

  public EnterAddressForm getEnterAddressForm() {
    return enterAddressForm;
  }

  public void setEnterAddressForm(EnterAddressForm enterAddressForm) {
    this.enterAddressForm = enterAddressForm;
  }
}
