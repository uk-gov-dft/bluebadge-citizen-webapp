package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import java.util.Optional;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.BaseForm;

public interface StepForm extends BaseForm {

  StepDefinition getAssociatedStep();

  default Optional<StepDefinition> determineNextStep() {
    return Optional.empty();
  }

  default Optional<StepDefinition> determineNextStep(Journey journey) {
    return Optional.empty();
  }

  /**
   * Used by journey to remove steps when a 'branching' step is changed. Some steps, such as
   * personal details do not get removed, others such as eligibility do. E.g. change eligibility
   * type in journey and all eligibility related steps are removed, but when eligibility is filled
   * back in the user will find the previously populated contact details.
   *
   * @param journey Journey being cleaned.
   * @return true to not remove step form from journey, false to remove.
   */
  boolean preserveStep(Journey journey);
}
