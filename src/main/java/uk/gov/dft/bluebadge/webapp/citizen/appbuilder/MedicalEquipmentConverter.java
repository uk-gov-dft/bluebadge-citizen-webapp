package uk.gov.dft.bluebadge.webapp.citizen.appbuilder;

import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.BulkyMedicalEquipmentTypeCodeField.OTHER;

import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.ChildUnder3;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MedicalEquipmentForm;

public class MedicalEquipmentConverter {

  static ChildUnder3 convert(Journey journey) {

    MedicalEquipmentForm form = journey.getFormForStep(StepDefinition.MEDICAL_EQUIPMENT);

    return ChildUnder3.builder()
        .bulkyMedicalEquipmentTypeCodes(form.getEquipment())
        .otherMedicalEquipment(getOtherDesc(form))
        .build();
  }

  static String getOtherDesc(MedicalEquipmentForm form) {
    return form.getEquipment().contains(OTHER) ? form.getOtherDescription() : null;
  }
}
