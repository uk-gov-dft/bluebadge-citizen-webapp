package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

@EqualsAndHashCode
public class MayBeEligibleForm implements StepForm {
  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.MAY_BE_ELIGIBLE;
  }
}
