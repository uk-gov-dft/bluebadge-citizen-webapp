package uk.gov.dft.bluebadge.webapp.citizen.model.form.walking;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

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
}
