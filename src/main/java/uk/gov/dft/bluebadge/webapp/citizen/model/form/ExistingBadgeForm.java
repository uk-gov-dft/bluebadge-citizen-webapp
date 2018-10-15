package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

@Data
@Builder
public class ExistingBadgeForm implements StepForm, Serializable {

  @NotNull(message = "{hasExistingBadge.NotNull}")
  private Boolean hasExistingBadge;

  private String ExistingBadgeNumber;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.EXISTING_BADGE;
  }
}
