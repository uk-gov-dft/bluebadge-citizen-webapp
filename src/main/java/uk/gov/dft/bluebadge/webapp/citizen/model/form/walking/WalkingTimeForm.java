package uk.gov.dft.bluebadge.webapp.citizen.model.form.walking;

import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingLengthOfTimeCodeField.CANTWALK;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.TREATMENT_LIST;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.WHERE_CAN_YOU_WALK;

import java.io.Serializable;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.WalkingLengthOfTimeCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

@Data
@Builder
public class WalkingTimeForm implements StepForm, Serializable {
  @NotNull WalkingLengthOfTimeCodeField walkingTime;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.WALKING_TIME;
  }

  @Override
  public Optional<StepDefinition> determineNextStep() {
    if (CANTWALK == walkingTime) {
      return Optional.of(TREATMENT_LIST);
    }

    return Optional.of(WHERE_CAN_YOU_WALK);
  }
}
