package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@EqualsAndHashCode
public class OrganisationMayBeEligibleForm implements StepForm, Serializable {
  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.ORGANISATION_MAY_BE_ELIGIBLE;
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return false;
  }
}
