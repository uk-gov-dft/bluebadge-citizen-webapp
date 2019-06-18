package uk.gov.dft.bluebadge.webapp.citizen.model.form.mainreason;

import java.io.Serializable;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Data
@Builder
@EqualsAndHashCode
public class MainReasonForm implements StepForm, Serializable {

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.MAIN_REASON;
  }

  @Override
  public Optional<StepDefinition> determineNextStep(Journey journey) {
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
      case HIDDEN:
        return Optional.of(StepDefinition.HIDDEN_DISABILITY);
    }
    return Optional.empty();
  }

  @NotNull private EligibilityCodeField mainReasonOption;

  @Override
  public boolean preserveStep(Journey journey) {
    return false;
  }
}
