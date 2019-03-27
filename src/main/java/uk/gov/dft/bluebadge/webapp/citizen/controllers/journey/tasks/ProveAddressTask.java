package uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.tasks;

import com.google.common.collect.ImmutableList;
import java.util.List;
import lombok.Getter;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.Task;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

public class ProveAddressTask implements Task {

  @Getter private final String titleCode;
  @Getter private final List<StepDefinition> steps;

  public ProveAddressTask(String titleCode, StepDefinition... steps) {
    this.titleCode = titleCode;
    this.steps = ImmutableList.copyOf(steps);
  }

  /** TaskDefinition only applicable for adults. So DOB must be answered and greater than 16 */
  @Override
  public StepDefinition getFirstStep(Journey journey) {
    if (journey.getEligibilityCode() == EligibilityCodeField.CHILDBULK
        || journey.getEligibilityCode() == EligibilityCodeField.CHILDVEHIC) {
      return null;
    }

    return Boolean.TRUE.equals(journey.isApplicantYoung()) ? null : steps.get(0);
  }
}
