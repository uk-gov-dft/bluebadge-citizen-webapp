package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.AFCS_COMPENSATION_SCHEME;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.ELIGIBLE;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.HIGHER_RATE_MOBILITY;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.MAIN_REASON;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.PIP_MOVING_AROUND;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
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
        return Optional.of(HIGHER_RATE_MOBILITY);
      case WPMS:
        return Optional.of(ELIGIBLE);
      case AFRFCS:
        return Optional.of(AFCS_COMPENSATION_SCHEME);
      case PIP:
        return Optional.of(PIP_MOVING_AROUND);
      default:
        return Optional.of(MAIN_REASON);
    }
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return false;
  }
}
