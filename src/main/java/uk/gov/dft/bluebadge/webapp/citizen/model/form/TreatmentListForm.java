package uk.gov.dft.bluebadge.webapp.citizen.model.form;

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

  public void addTreatment(TreatmentAddForm treatmentAddForm) {
    if (null == treatments) {
      treatments = new ArrayList<>();
    }
    treatments.add(treatmentAddForm);
  }

  public List<TreatmentAddForm> getTreatments() {
    return treatments == null ? new ArrayList<>() : treatments;
  }
}
