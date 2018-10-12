package uk.gov.dft.bluebadge.webapp.citizen.model.form.walking;

import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.MobilityAidAddForm;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class MedicationListForm implements Serializable, StepForm {

  List<MedicationAddForm> medications;

  @NotNull(message = "{NotNull.hasMedications}")
  private String hasMedication;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.MOBILITY_AID_LIST;
  }
}
