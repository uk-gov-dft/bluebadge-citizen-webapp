package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.JourneyArtifact;

@Data
@Builder
public class ProvidePhotoForm implements StepForm, Serializable {

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
