package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

@Data
@Builder
public class ChooseYourCouncilForm implements StepForm, Serializable {

  @NotBlank(message = "{councilShortCode.NotBank}")
  private String councilShortCode;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.CHOOSE_COUNCIL;
  }
}
