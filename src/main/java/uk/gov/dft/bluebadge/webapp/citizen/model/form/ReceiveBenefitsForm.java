package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

@Data
@Builder
public class ReceiveBenefitsForm implements StepForm, Serializable {
  enum BenefitType {
    pip,
    dla,
    none
  }

  @NotNull private BenefitType benefitType;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.RECEIVE_BENEFITS;
  }

  @Override
  public Optional<StepDefinition> determineNextStep() {
    switch (benefitType) {
      case pip:
        return Optional.of(StepDefinition.ELIGIBLE);
      case dla:
        return Optional.of(StepDefinition.ELIGIBLE);
      default:
        return Optional.of(StepDefinition.MAY_BE_ELIGIBLE);
    }
  }
}
