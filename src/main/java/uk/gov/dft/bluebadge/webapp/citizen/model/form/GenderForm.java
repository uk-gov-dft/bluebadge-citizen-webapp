package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.GenderCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Data
@Builder
public class GenderForm implements StepForm, Serializable {

  @NotNull(message = "NotNull.gender")
  private GenderCodeField gender;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.GENDER;
  }

  @Override
  public Optional<StepDefinition> determineNextStep(Journey journey) {
    EligibilityCodeField benefitType = journey.getReceiveBenefitsForm().getBenefitType();
    switch (benefitType) {
      case DLA:
      case WPMS:
      case AFRFCS:
      case PIP:
      case BLIND:
        return Optional.of(StepDefinition.DECLARATIONS);
      default:
        return StepForm.super.determineNextStep(journey);
    }
  }
}
