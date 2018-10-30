package uk.gov.dft.bluebadge.webapp.citizen.controllers;

import static uk.gov.dft.bluebadge.webapp.citizen.model.Journey.JOURNEY_SESSION_KEY;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Application;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.ApplicationTypeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Benefit;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Blind;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.BulkyMedicalEquipmentTypeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.ChildUnder3;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Contact;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.DisabilityArms;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Eligibility;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.HealthcareProfessional;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Medication;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Party;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.PartyTypeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Person;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Treatment;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingAid;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingDifficulty;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingDifficultyTypeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingLengthOfTimeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Mappings;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.RouteMaster;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ApplicantNameForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ContactDetailsForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.DeclarationForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ExistingBadgeForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthcareProfessionalAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MobilityAidAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.TreatmentAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.YourIssuingAuthorityForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.walking.MedicationAddForm;
import uk.gov.dft.bluebadge.webapp.citizen.service.ApplicationManagementService;

@Controller
@RequestMapping(Mappings.URL_DECLARATIONS)
public class DeclarationSubmitController implements StepController {

  private static final String TEMPLATE_DECLARATION = "application-end/declaration";

  private final ApplicationManagementService appService;
  private final RouteMaster routeMaster;

  @Autowired
  public DeclarationSubmitController(
      ApplicationManagementService appService, RouteMaster routeMaster) {
    this.appService = appService;
    this.routeMaster = routeMaster;
  }

  @GetMapping
  public String showDeclaration(Model model, @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey) {

    if (!journey.isValidState(getStepDefinition())) {
      return routeMaster.backToCompletedPrevious();
    }

    if (!model.containsAttribute("formRequest")) {
      model.addAttribute("formRequest", DeclarationForm.builder().build());
    }

    return TEMPLATE_DECLARATION;
  }

  @PostMapping
  public String submitDeclaration(
      @ModelAttribute(JOURNEY_SESSION_KEY) Journey journey,
      @Valid @ModelAttribute("formRequest") DeclarationForm declarationForm,
      BindingResult bindingResult,
      RedirectAttributes attr) {

    if (bindingResult.hasErrors()) {
      return routeMaster.redirectToOnBindingError(this, declarationForm, bindingResult, attr);
    }

    appService.create(getDummyApplication(journey));

    return routeMaster.redirectToOnSuccess(declarationForm);
  }

  public Application getDummyApplication(Journey journey) {
    ApplicantNameForm applicantNameForm = journey.getApplicantNameForm();
    YourIssuingAuthorityForm yourIssuingAuthorityForm = journey.getYourIssuingAuthorityForm();
    ContactDetailsForm contactDetailsForm = journey.getContactDetailsForm();

    EligibilityCodeField eligibility = journey.getEligibilityCode();

    String condDesc = journey.getDescriptionOfCondition();

    String nino = null;
    if (null != journey.getNinoForm() && null != journey.getNinoForm().getNino()) {
      nino = journey.getNinoForm().getNino().toUpperCase();
    }

    Person person =
        new Person()
            .badgeHolderName(applicantNameForm.getFullName())
            .nameAtBirth(applicantNameForm.getBirthName())
            .dob(journey.getDateOfBirthForm().getDateOfBirth().getLocalDate())
            .genderCode(journey.getGenderForm().getGender())
            .nino(nino);

    Party party =
        new Party()
            .typeCode(PartyTypeCodeField.PERSON)
            .contact(
                new Contact()
                    .buildingStreet(journey.getEnterAddressForm().getBuildingAndStreet())
                    .line2(journey.getEnterAddressForm().getOptionalAddress())
                    .townCity(journey.getEnterAddressForm().getTownOrCity())
                    .postCode(journey.getEnterAddressForm().getPostcode())
                    .fullName(contactDetailsForm.getFullName())
                    .primaryPhoneNumber(contactDetailsForm.getPrimaryPhoneNumber())
                    .secondaryPhoneNumber(contactDetailsForm.getSecondaryPhoneNumber())
                    .emailAddress(contactDetailsForm.getEmailAddress()))
            .person(person);

    List<HealthcareProfessional> healthcareProfessionals = null;
    if (null != journey.getHealthcareProfessionalListForm()
        && null != journey.getHealthcareProfessionalListForm().getHealthcareProfessionals()) {
      healthcareProfessionals = new ArrayList<>();
      for (HealthcareProfessionalAddForm item :
          journey.getHealthcareProfessionalListForm().getHealthcareProfessionals()) {
        healthcareProfessionals.add(
            new HealthcareProfessional()
                .name(item.getHealthcareProfessionalName())
                .location(item.getHealthcareProfessionalLocation()));
      }
    }

    Eligibility eligibilityObject = null;
    switch (eligibility) {
      case WALKD:
        eligibilityObject = buildWalkingEligibility(journey, healthcareProfessionals);

        break;
      case PIP:
      case DLA:
      case WPMS:
        eligibilityObject =
            new Eligibility().typeCode(eligibility).benefit(new Benefit().isIndefinite(true));
        break;
      case AFRFCS:
        eligibilityObject = new Eligibility().typeCode(eligibility);
        break;
      case BLIND:
        eligibilityObject = new Eligibility().typeCode(eligibility).blind(new Blind());
        break;
      case ARMS:
        eligibilityObject =
            new Eligibility()
                .typeCode(eligibility)
                .descriptionOfConditions(condDesc)
                .disabilityArms(new DisabilityArms().isAdaptedVehicle(false));
        break;
      case CHILDBULK:
        eligibilityObject =
            new Eligibility()
                .typeCode(eligibility)
                .descriptionOfConditions(condDesc)
                .childUnder3(
                    new ChildUnder3()
                        .bulkyMedicalEquipmentTypeCode(BulkyMedicalEquipmentTypeCodeField.NONE))
                .healthcareProfessionals(healthcareProfessionals);
        break;
      case CHILDVEHIC:
        eligibilityObject =
            new Eligibility()
                .descriptionOfConditions(condDesc)
                .typeCode(eligibility)
                .healthcareProfessionals(healthcareProfessionals);
        break;
      case TERMILL:
      case NONE:
        // Invalid to get here with no eligibility if person route
        // This code is all temporary too.
        throw new IllegalStateException("Invalid eligibility:" + eligibility);
    }

    Application.ApplicationBuilder application = Application.builder();

    ExistingBadgeForm existingBadgeForm = journey.getExistingBadgeForm();
    if (existingBadgeForm != null && existingBadgeForm.getBadgeNumber() != null) {
      application.existingBadgeNumber(existingBadgeForm.getBadgeNumber());
    }

    return application
        .applicationTypeCode(ApplicationTypeCodeField.NEW)
        .localAuthorityCode(yourIssuingAuthorityForm.getLocalAuthorityShortCode())
        .paymentTaken(false)
        .party(party)
        .eligibility(eligibilityObject)
        .build();
  }

  private Eligibility buildWalkingEligibility(
      Journey journey, List<HealthcareProfessional> healthcareProfessionals) {
    Eligibility eligibilityObject;
    List<WalkingAid> walkingAids = null;
    if (null != journey.getMobilityAidListForm()
        && "yes".equals(journey.getMobilityAidListForm().getHasWalkingAid())) {
      walkingAids = new ArrayList<>();
      for (MobilityAidAddForm mobilityAidAddForm :
          journey.getMobilityAidListForm().getMobilityAids()) {
        walkingAids.add(
            new WalkingAid()
                .usage(mobilityAidAddForm.getUsage())
                .howProvidedCode(mobilityAidAddForm.getHowProvidedCodeField())
                .description(mobilityAidAddForm.getAidTypeDescription()));
      }
    }
    List<Treatment> treatments = null;
    if (null != journey.getTreatmentListForm()
        && "yes".equals(journey.getTreatmentListForm().getHasTreatment())) {
      treatments = new ArrayList<>();
      for (TreatmentAddForm treatmentAddForm : journey.getTreatmentListForm().getTreatments()) {
        treatments.add(
            new Treatment()
                .time(treatmentAddForm.getTreatmentWhen())
                .description(treatmentAddForm.getTreatmentDescription()));
      }
    }
    List<Medication> medications = null;
    if (null != journey.getMedicationListForm()
        && "yes".equals(journey.getMedicationListForm().getHasMedication())) {
      medications = new ArrayList<>();
      for (MedicationAddForm medicationAddForm : journey.getMedicationListForm().getMedications()) {
        medications.add(
            new Medication()
                .name(medicationAddForm.getName())
                .quantity(medicationAddForm.getDosage())
                .isPrescribed(medicationAddForm.getPrescribedValue())
                .frequency(medicationAddForm.getFrequency()));
      }
    }

    WalkingLengthOfTimeCodeField walkingTime = journey.getWalkingTimeForm().getWalkingTime();

    List<WalkingDifficultyTypeCodeField> walkingDifficulties =
        journey.getWhatMakesWalkingDifficultForm().getWhatWalkingDifficulties();
    String otherDesc =
        walkingDifficulties.contains(WalkingDifficultyTypeCodeField.SOMELSE)
            ? journey.getWhatMakesWalkingDifficultForm().getSomethingElseDescription()
            : null;

    eligibilityObject =
        new Eligibility()
            .typeCode(EligibilityCodeField.WALKD)
            .descriptionOfConditions(journey.getDescriptionOfCondition())
            .walkingDifficulty(
                new WalkingDifficulty()
                    .walkingLengthOfTimeCode(walkingTime)
                    // .walkingSpeedCode(null)
                    .typeCodes(walkingDifficulties)
                    .otherDescription(otherDesc)
                    .walkingAids(walkingAids)
                    .treatments(treatments)
                    .medications(medications))
            .healthcareProfessionals(healthcareProfessionals);

    return eligibilityObject;
  }

  @Override
  public StepDefinition getStepDefinition() {
    return StepDefinition.DECLARATIONS;
  }
}
