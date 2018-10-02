package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

@Data
@Builder
public class WhereCanYouWalkForm implements StepForm, Serializable {

  @NotBlank
  @Size(max = 100)
  private String destinationToHome;

  @NotBlank
  @Size(max = 100)
  private String timeToDestination;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.WHERE_CAN_YOU_WALK;
  }
}
