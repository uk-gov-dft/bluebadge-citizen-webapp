package uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason;

import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

@Data
@Builder
public class MainReasonForm implements StepForm {

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.MAIN_REASON;
  }

  @Override
  public Optional<StepDefinition> determineNextStep() {
    switch (mainReasonOption) {
      case TERMILL:
        return Optional.of(StepDefinition.CONTACT_COUNCIL);
      case WALKD:
        return Optional.of(StepDefinition.WALKING_DIFFICULTY);
      case ARMS:
      case CHILDBULK:
      case CHILDVEHIC:
        return Optional.of(StepDefinition.MAY_BE_ELIGIBLE);
      case BLIND:
        return Optional.of(StepDefinition.ELIGIBLE);
      case NONE:
        return Optional.of(StepDefinition.NOT_ELIGIBLE);
    }
    return Optional.empty();
  }

  @NotNull private EligibilityCodeField mainReasonOption;
}
