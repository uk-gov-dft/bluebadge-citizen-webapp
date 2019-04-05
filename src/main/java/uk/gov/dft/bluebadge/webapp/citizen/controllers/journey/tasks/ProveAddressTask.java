package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.tasks;

import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Task;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@EqualsAndHashCode(callSuper = true)
public class ProveAddressTask extends Task {

  public ProveAddressTask(String titleCode, StepDefinition... steps) {
    super(titleCode, steps);
  }

  /** TaskDefinition only applicable for adults. So DOB must be answered and greater than 16 */
  @Override
  public StepDefinition getFirstStep(Journey journey) {
    if (journey.getEligibilityCode() == EligibilityCodeField.CHILDBULK
        || journey.getEligibilityCode() == EligibilityCodeField.CHILDVEHIC) {
      return null;
    }

    return Boolean.TRUE.equals(journey.isApplicantYoung()) ? null : getSteps().get(0);
  }
}
