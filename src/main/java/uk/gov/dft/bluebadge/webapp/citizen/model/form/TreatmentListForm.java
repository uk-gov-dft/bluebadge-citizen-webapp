package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode
public class TreatmentListForm implements Serializable, StepForm {

  List<TreatmentAddForm> treatments;

  @NotNull private String hasTreatment;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.TREATMENT_LIST;
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return false;
  }
}
