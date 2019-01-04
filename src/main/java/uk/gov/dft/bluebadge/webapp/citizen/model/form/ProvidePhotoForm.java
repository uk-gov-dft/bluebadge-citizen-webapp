package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.JourneyArtifact;

@Data
@Builder
public class ProvidePhotoForm implements StepForm, ArtifactForm, Serializable {

  private JourneyArtifact journeyArtifact;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.PROVIDE_PHOTO;
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return true;
  }

  /**
   * If the eligibility is any of the child based codes then skip the next PROVE_ADDRESS step
   *
   * @param journey
   * @return
   */
  @Override
  public Optional<StepDefinition> determineNextStep(Journey journey) {
    if (journey.getEligibilityCode() == EligibilityCodeField.CHILDBULK
        || journey.getEligibilityCode() == EligibilityCodeField.CHILDVEHIC) {
      return Optional.of(StepDefinition.DECLARATIONS);
    }

    return Optional.empty();
  }
}
