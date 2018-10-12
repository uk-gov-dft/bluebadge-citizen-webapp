package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.CHILDBULK;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.CHILDVEHIC;

@Data
@Builder
public class HealthConditionsForm implements StepForm, Serializable {
  @NotNull
  @Size(min = 1, max = 9000)
  String descriptionOfConditions;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.HEALTH_CONDITIONS;
  }

  @Override
  public Optional<StepDefinition> determineNextStep(Journey journey) {
    if (EligibilityCodeField.WALKD.equals(journey.getEligibilityCode())) {
      return Optional.of(StepDefinition.WHAT_WALKING_DIFFICULTIES);
    }
    if (journey.getEligibilityCode() == CHILDBULK || journey.getEligibilityCode() == CHILDVEHIC){
      return Optional.of(StepDefinition.HEALTHCARE_PROFESSIONAL_LIST);
    }
    return Optional.of(StepDefinition.DECLARATIONS);
  }
}
