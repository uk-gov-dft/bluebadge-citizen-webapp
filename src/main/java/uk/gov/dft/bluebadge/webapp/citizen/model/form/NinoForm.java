package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import javax.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.validation.ValidationPatterns;

@Builder
@Data
public class NinoForm implements StepForm {

  @Pattern(regexp = ValidationPatterns.NINO_CASE_INSENSITIVE)
  private String nino;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.NINO;
  }
}