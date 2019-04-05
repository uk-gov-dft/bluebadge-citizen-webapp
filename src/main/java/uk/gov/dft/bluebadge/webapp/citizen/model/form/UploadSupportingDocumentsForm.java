package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.JourneyArtifact;

@Data
@Builder
public class UploadSupportingDocumentsForm implements StepForm, ArtifactForm, Serializable {

  @NotNull private Boolean hasDocuments;

  private List<JourneyArtifact> journeyArtifacts = new ArrayList<>();

  public boolean hasDocuments() {
    return (null != hasDocuments) && hasDocuments;
  }

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.UPLOAD_SUPPORTING_DOCUMENTS;
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

  public static class UploadSupportingDocumentsFormBuilder {
    private List<JourneyArtifact> journeyArtifacts = new ArrayList<>();
  }
}
