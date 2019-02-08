package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import java.util.Optional;
import javax.validation.constraints.AssertTrue;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Builder
@Data
@EqualsAndHashCode
public class DeclarationSubmitForm implements StepForm, Serializable {

  @AssertTrue(message = "{declarationPage.validation.declaration}")
  private Boolean agreed;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.DECLARATIONS;
  }

  @Override
  public Optional<StepDefinition> determineNextStep(Journey journey) {
    if (journey.isPaymentsEnabled()) {
      return Optional.of(StepDefinition.PAY_FOR_THE_BADGE);
    } else {
      return Optional.of(StepDefinition.SUBMITTED);
    }
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return false;
  }
}
