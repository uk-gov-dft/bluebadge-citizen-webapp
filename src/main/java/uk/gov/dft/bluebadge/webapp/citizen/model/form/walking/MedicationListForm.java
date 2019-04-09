package uk.gov.dft.bluebadge.webapp.citizen.model.form.walking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Data
@Builder
@EqualsAndHashCode
public class MedicationListForm implements Serializable, StepForm {

  List<MedicationAddForm> medications;

  @NotNull(message = "{NotNull.hasMedication}")
  private String hasMedication;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.MEDICATION_LIST;
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return false;
  }

  public void addMedication(MedicationAddForm medicationAddForm) {
    if (null == medications) {
      medications = new ArrayList<>();
    }
    medications.add(medicationAddForm);
  }

  public List<MedicationAddForm> getMedications() {
    return medications == null ? new ArrayList<>() : medications;
  }
}
