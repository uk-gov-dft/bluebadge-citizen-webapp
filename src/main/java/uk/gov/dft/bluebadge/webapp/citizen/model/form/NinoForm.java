package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.validation.ValidationPatterns;

import java.io.Serializable;

@Builder
@Data
@EqualsAndHashCode
public class NinoForm implements StepForm, Serializable {

  @NotBlank(message = "{field.nino.NotBlank}")
  @Pattern(regexp = ValidationPatterns.NINO_CASE_INSENSITIVE)
  private String nino;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.NINO;
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return true;
  }
}
