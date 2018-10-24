package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.MAIN_REASON;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.PIP_MOVING_AROUND;

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
        return Optional.of(StepDefinition.HIGHER_RATE_MOBILITY);
      case WPMS:
        return Optional.of(StepDefinition.ELIGIBLE);
      case AFRFCS:
        return Optional.of(StepDefinition.AFCS_COMPENSATION_SCHEME);
      case PIP:
        return Optional.of(PIP_MOVING_AROUND);
      default:
        return Optional.of(MAIN_REASON);
    }
  }

  @Override
  public Set<StepDefinition> getCleanUpSteps(Journey journey) {

    // Clean all eligibility based steps - leave personal details.
    Set<StepDefinition> steps = new HashSet<>();
    steps.addAll(getAssociatedStep().getNext());
    steps.addAll(StepDefinition.CONTACT_DETAILS.getNext());

    return steps;
  }
}
