package uk.gov.dft.bluebadge.webapp.citizen.model.form.walking;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingLengthOfTimeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Data
@Builder
@EqualsAndHashCode
public class WalkingTimeForm implements StepForm, Serializable {
  @NotNull WalkingLengthOfTimeCodeField walkingTime;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.WALKING_TIME;
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return false;
  }
}
