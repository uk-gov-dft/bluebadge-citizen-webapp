package uk.gov.dft.bluebadge.webapp.citizen.appbuilder;

import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.CHILDBULK;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.CHILDVEHIC;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.WALKD;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Benefit;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Blind;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.DisabilityArms;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Eligibility;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.HealthcareProfessional;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.HealthcareProfessionalListForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ProveBenefitForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.arms.ArmsAdaptedVehicleForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.arms.ArmsHowOftenDriveForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.blind.RegisteredCouncilForm;

class EligibilityConverter {

  private EligibilityConverter() {}

  static Eligibility convert(Journey journey) {

    Eligibility.EligibilityBuilder eligibility = Eligibility.builder();
    EligibilityCodeField eligibilityType = journey.getEligibilityCode();
    switch (eligibilityType) {
      case WALKD:
        eligibility
            .typeCode(eligibilityType)
            .descriptionOfConditions(journey.getDescriptionOfCondition())
            .walkingDifficulty(WalkingConverter.convert(journey));
        break;
      case PIP:
      case DLA:
        ProveBenefitForm proveBenefit = journey.getFormForStep(StepDefinition.PROVE_BENEFIT);
        Benefit.BenefitBuilder benefit = Benefit.builder().isIndefinite(proveBenefit.getHasProof());

        if (!proveBenefit.getHasProof()) {
          benefit.expiryDate(proveBenefit.getAwardEndDate().getLocalDate());
        }

        eligibility.typeCode(eligibilityType).benefit(benefit.build());
        break;
      case WPMS:
        eligibility.typeCode(eligibilityType).benefit(Benefit.builder().isIndefinite(true).build());
        break;
      case AFRFCS:
        eligibility.typeCode(eligibilityType);
        break;
      case BLIND:
        RegisteredCouncilForm registeredCouncil =
            journey.getFormForStep(StepDefinition.REGISTERED_COUNCIL);
        eligibility
            .typeCode(eligibilityType)
            .blind(
                Blind.builder()
                    .registeredAtLaId(
                        registeredCouncil.getLocalAuthorityForRegisteredBlind().getShortCode())
                    .build());
        break;
      case ARMS:
        ArmsAdaptedVehicleForm adaptedVehicleForm =
            journey.getFormForStep(StepDefinition.ARMS_DRIVE_ADAPTED_VEHICLE);
        ArmsHowOftenDriveForm howOftenDriveForm =
            journey.getFormForStep(StepDefinition.ARMS_HOW_OFTEN_DRIVE);
        eligibility
            .typeCode(eligibilityType)
            .descriptionOfConditions(journey.getDescriptionOfCondition())
            .disabilityArms(
                DisabilityArms.builder()
                    .isAdaptedVehicle(adaptedVehicleForm.getHasAdaptedVehicle())
                    .adaptedVehicleDescription(adaptedVehicleForm.getAdaptedVehicleDescription())
                    .drivingFrequency(howOftenDriveForm.getHowOftenDrive())
                    .build());
        break;
      case CHILDBULK:
        eligibility
            .typeCode(eligibilityType)
            .descriptionOfConditions(journey.getDescriptionOfCondition())
            .childUnder3(MedicalEquipmentConverter.convert(journey));
        break;
      case CHILDVEHIC:
        eligibility
            .descriptionOfConditions(journey.getDescriptionOfCondition())
            .typeCode(eligibilityType);
        break;
      case TERMILL:
      case NONE:
      default:
        // Invalid to get here with no eligibility if person route
        // This code is all temporary too.
        throw new IllegalStateException("Invalid eligibility:" + eligibilityType);
    }

    // Healthcare Professionals
    if (EnumSet.of(CHILDBULK, CHILDVEHIC, WALKD).contains(eligibilityType)) {
      eligibility.healthcareProfessionals(getHealthcareProfessionals(journey));
    }
    return eligibility.build();
  }

  private static List<HealthcareProfessional> getHealthcareProfessionals(Journey journey) {

    HealthcareProfessionalListForm listForm =
        journey.getFormForStep(StepDefinition.HEALTHCARE_PROFESSIONAL_LIST);
    List<HealthcareProfessional> healthcareProfessionals = new ArrayList<>();
    listForm
        .getHealthcareProfessionals()
        .forEach(
            i ->
                healthcareProfessionals.add(
                    HealthcareProfessional.builder()
                        .location(i.getHealthcareProfessionalLocation())
                        .name(i.getHealthcareProfessionalName())
                        .build()));
    return healthcareProfessionals;
  }
}
