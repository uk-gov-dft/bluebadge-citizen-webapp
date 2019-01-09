package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.JourneyArtifact;

@Data
@Builder
public class UploadBenefitForm implements StepForm, ArtifactForm, Serializable {

  private List<JourneyArtifact> journeyArtifacts = new ArrayList<>();

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.UPLOAD_BENEFIT;
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return true;
  }

  public void addJourneyArtifact(JourneyArtifact journeyArtifact) {
    if (null == journeyArtifacts) {
      journeyArtifacts = new ArrayList<>();
    }
    journeyArtifacts.add(journeyArtifact);
  }

  public static class UploadBenefitFormBuilder {
    private List<JourneyArtifact> journeyArtifacts = new ArrayList<>();
  }
}
