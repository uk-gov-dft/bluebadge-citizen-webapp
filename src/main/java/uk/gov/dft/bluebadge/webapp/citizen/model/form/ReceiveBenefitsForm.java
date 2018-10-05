package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Data
@Builder
public class ReceiveBenefitsForm implements StepForm, Serializable {

  @NotNull private EligibilityCodeField benefitType;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.RECEIVE_BENEFITS;
  }

  @Override
  public Optional<StepDefinition> determineNextStep() {
    switch (benefitType) {
      case DLA:
        return Optional.of(StepDefinition.HIGHER_RATE_MOBILITY);
      case WPMS:
        return Optional.of(StepDefinition.ELIGIBLE);
      case AFRFCS:
        return Optional.of(StepDefinition.AFCS_COMPENSATION_SCHEME);
      case PIP:
        return Optional.of(StepDefinition.PIP_MOVING_AROUND);
      default:
        return Optional.of(StepDefinition.MAIN_REASON);
    }
  }

  @Override
  public List<StepDefinition> getCleanUpSteps(Journey journey) {
    return Arrays.asList(StepDefinition.MAIN_REASON);
  }
}
