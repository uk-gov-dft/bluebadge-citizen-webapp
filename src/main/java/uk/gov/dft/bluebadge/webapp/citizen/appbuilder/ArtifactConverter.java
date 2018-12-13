package uk.gov.dft.bluebadge.webapp.citizen.appbuilder;

import java.util.ArrayList;
import java.util.List;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Artifact;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.ArtifactType;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.JourneyArtifact;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ProveIdentityForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ProvidePhotoForm;

class ArtifactConverter {

  private ArtifactConverter() {}

  static List<Artifact> convert(Journey journey) {
    List<Artifact> result = new ArrayList<>();

    ProveIdentityForm proveIdentityForm = journey.getFormForStep(StepDefinition.PROVE_IDENTITY);
    JourneyArtifact journeyArtifact = proveIdentityForm.getJourneyArtifact();
    convertArtifact(result, journeyArtifact, ArtifactType.PROOF_ID);

    ProvidePhotoForm providePhotoForm = journey.getFormForStep(StepDefinition.PROVIDE_PHOTO);
    journeyArtifact = providePhotoForm.getJourneyArtifact();
    convertArtifact(result, journeyArtifact, ArtifactType.PHOTO);

    return result;
  }

  private static void convertArtifact(List<Artifact> result, JourneyArtifact journeyArtifact, ArtifactType artifactType) {
    if (null != journeyArtifact) {
      result.add(
          Artifact.builder()
              .type(artifactType)
              .link(journeyArtifact.getUrl().toString())
              .build());
    }
  }
}
