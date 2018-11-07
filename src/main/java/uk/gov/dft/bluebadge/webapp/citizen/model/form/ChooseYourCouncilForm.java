package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Data
@Builder
@EqualsAndHashCode
public class ChooseYourCouncilForm implements StepForm, Serializable {

  @NotBlank(message = "{councilShortCode.NotBank}")
  private String councilShortCode;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.CHOOSE_COUNCIL;
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return true;
  }
}
