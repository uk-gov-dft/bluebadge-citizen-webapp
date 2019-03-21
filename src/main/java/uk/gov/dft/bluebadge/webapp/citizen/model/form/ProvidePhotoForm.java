package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.PROVE_ADDRESS;

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
}
