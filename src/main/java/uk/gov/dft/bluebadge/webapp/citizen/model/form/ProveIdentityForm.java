package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Data
@Builder
public class ProveIdentityForm implements StepForm, Serializable {

  @NotBlank(message = "{NotNull.document}")
  private String documentId;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.PROVE_IDENTITY;
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return true;
  }
}
