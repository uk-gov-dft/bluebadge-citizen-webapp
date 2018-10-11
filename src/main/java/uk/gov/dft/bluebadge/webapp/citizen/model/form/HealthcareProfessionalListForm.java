package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class HealthcareProfessionalListForm implements Serializable, StepForm {

  List<HealthcareProfessionalAddForm> healthcareProfessionals;

  @NotNull private String hasHealthcareProfessional;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.HEALTHCARE_PROFESSIONAL_LIST;
  }
}
