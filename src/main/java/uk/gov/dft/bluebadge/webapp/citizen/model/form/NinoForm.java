package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import static uk.gov.dft.bluebadge.common.util.ValidationPattern.NINO_CASE_INSENSITIVE;

import java.io.Serializable;
import javax.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Builder
@Data
@EqualsAndHashCode
public class NinoForm implements StepForm, Serializable {

  @Pattern(regexp = NINO_CASE_INSENSITIVE)
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
