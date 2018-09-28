package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import javax.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

@Builder
@Data
public class NinoForm implements StepForm {

  @Pattern(
    regexp =
        "^(?!BG)(?!GB)(?!NK)(?!KN)(?!TN)(?!NT)(?!ZZ)(?:[A-CEGHJ-PR-TW-Z][A-CEGHJ-NPR-TW-Z])(?:\\s*\\d\\s*){6}([A-D]|\\s)$"
  )
  private String nino;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.NINO;
  }
}
