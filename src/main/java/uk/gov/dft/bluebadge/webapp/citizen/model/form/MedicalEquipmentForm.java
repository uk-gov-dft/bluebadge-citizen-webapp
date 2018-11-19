package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.BulkyMedicalEquipmentTypeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Data
@Builder
public class MedicalEquipmentForm implements StepForm, Serializable {

  @NotEmpty private List<BulkyMedicalEquipmentTypeCodeField> equipment;
  private String otherDescription;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.MEDICAL_EQUIPMENT;
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return false;
  }
}
