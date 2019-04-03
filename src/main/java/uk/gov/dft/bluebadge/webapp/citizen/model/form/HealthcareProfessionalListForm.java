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
public class HealthcareProfessionalListForm implements Serializable, StepForm {

  List<HealthcareProfessionalAddForm> healthcareProfessionals;

  @NotNull private String hasHealthcareProfessional;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.HEALTHCARE_PROFESSIONAL_LIST;
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return false;
  }

  public void addHealthcareProfessional(
      HealthcareProfessionalAddForm healthcareProfessionalAddForm) {
    if (null == healthcareProfessionals) {
      healthcareProfessionals = new ArrayList<>();
    }
    healthcareProfessionals.add(healthcareProfessionalAddForm);
  }

  public static class HealthcareProfessionalListFormBuilder {
    private List<HealthcareProfessionalAddForm> healthcareProfessionals = new ArrayList<>();
  }
}
