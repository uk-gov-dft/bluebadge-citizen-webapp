package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

@EqualsAndHashCode
public class OrganisationMayBeEligibleForm implements StepForm {
  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.ORGANISATION_MAY_BE_ELIGIBLE;
  }
}
