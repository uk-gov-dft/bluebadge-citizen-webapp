package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey;

import java.util.List;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

public class ProveAddressTask implements TaskConfig {
  /** Task only applicable for adults. So DOB must be answered and greater than 16 */
  @Override
  public StepDefinition getFirstStep(Journey journey, List<StepDefinition> steps) {
    if (journey.getEligibilityCode() == EligibilityCodeField.CHILDBULK
        || journey.getEligibilityCode() == EligibilityCodeField.CHILDVEHIC) {
      return null;
    }

    return Boolean.TRUE.equals(journey.isApplicantYoung()) ? null : steps.get(0);
  }
}
