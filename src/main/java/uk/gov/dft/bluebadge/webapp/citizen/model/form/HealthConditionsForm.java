package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.ARMS;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.CHILDBULK;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.CHILDVEHIC;

import java.io.Serializable;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Data
@Builder
@EqualsAndHashCode
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
    if (journey.getEligibilityCode() == CHILDVEHIC) {
      return Optional.of(StepDefinition.HEALTHCARE_PROFESSIONAL_LIST);
    }
    if (journey.getEligibilityCode() == ARMS) {
      return Optional.of(StepDefinition.ARMS_HOW_OFTEN_DRIVE);
    }
    if (journey.getEligibilityCode() == CHILDBULK) {
      return Optional.of(StepDefinition.MEDICAL_EQUIPMENT);
    }
    return Optional.of(StepDefinition.DECLARATIONS);
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return false;
  }
}
