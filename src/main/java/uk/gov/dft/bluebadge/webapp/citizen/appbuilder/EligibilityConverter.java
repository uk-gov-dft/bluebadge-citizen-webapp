package uk.gov.dft.bluebadge.webapp.citizen.appbuilder;

import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Benefit;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Blind;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.BulkyMedicalEquipmentTypeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.ChildUnder3;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.DisabilityArms;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Eligibility;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

class EligibilityConverter {

  static Eligibility convert(Journey journey) {

    Eligibility eligibility = null;
    EligibilityCodeField eligibilityType = journey.getEligibilityCode();
    switch (eligibilityType) {
      case WALKD:
        eligibility =
            Eligibility.builder()
                .typeCode(eligibilityType)
                .walkingDifficulty(WalkingConverter.convert(journey))
                .build();

        break;
      case PIP:
      case DLA:
      case WPMS:
        eligibility =
            Eligibility.builder()
                .typeCode(eligibilityType)
                .benefit(Benefit.builder().isIndefinite(true).build())
                .build();
        break;
      case AFRFCS:
        eligibility = Eligibility.builder().typeCode(eligibilityType).build();
        break;
      case BLIND:
        eligibility =
            Eligibility.builder().typeCode(eligibilityType).blind(Blind.builder().build()).build();
        break;
      case ARMS:
        eligibility =
            Eligibility.builder()
                .typeCode(eligibilityType)
                .descriptionOfConditions(journey.getDescriptionOfCondition())
                .disabilityArms(DisabilityArms.builder().isAdaptedVehicle(false).build())
                .build();
        break;
      case CHILDBULK:
        eligibility =
            Eligibility.builder()
                .typeCode(eligibilityType)
                .descriptionOfConditions(journey.getDescriptionOfCondition())
                .childUnder3(
                    ChildUnder3.builder()
                        .bulkyMedicalEquipmentTypeCode(BulkyMedicalEquipmentTypeCodeField.NONE)
                        .build())
                .build();
        break;
      case CHILDVEHIC:
        eligibility =
            Eligibility.builder()
                .descriptionOfConditions(journey.getDescriptionOfCondition())
                .typeCode(eligibilityType)
                .build();
        break;
      case TERMILL:
      case NONE:
        // Invalid to get here with no eligibility if person route
        // This code is all temporary too.
        throw new IllegalStateException("Invalid eligibility:" + eligibilityType);
    }
    return eligibility;
  }
}
