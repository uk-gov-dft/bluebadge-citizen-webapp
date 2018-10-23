package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

public class EligibleForm implements StepForm {
  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.ELIGIBLE;
  }
}
