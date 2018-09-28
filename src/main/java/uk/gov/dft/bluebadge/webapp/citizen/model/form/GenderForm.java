package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.GenderCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

@Data
@Builder
public class GenderForm implements StepForm, Serializable {

  @NotNull(message = "NotNull.gender")
  private GenderCodeField gender;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.GENDER;
  }
}
