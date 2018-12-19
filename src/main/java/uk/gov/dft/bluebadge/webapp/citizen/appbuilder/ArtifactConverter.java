package uk.gov.dft.bluebadge.webapp.citizen.appbuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Artifact;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.ArtifactType;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ArtifactForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ProveIdentityForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ProvidePhotoForm;

class ArtifactConverter {

  private ArtifactConverter() {}

  static List<Artifact> convert(Journey journey) {
    List<Artifact> result = new ArrayList<>();

    ProveIdentityForm proveIdentityForm = journey.getFormForStep(StepDefinition.PROVE_IDENTITY);
    convertArtifact(result, proveIdentityForm, ArtifactType.PROOF_ID);

    ProvidePhotoForm providePhotoForm = journey.getFormForStep(StepDefinition.PROVIDE_PHOTO);
    convertArtifact(result, providePhotoForm, ArtifactType.PHOTO);

    return result;
  }

  private static void convertArtifact(
      List<Artifact> result, ArtifactForm artifactForm, ArtifactType artifactType) {
    artifactForm
        .getJourneyArtifacts()
        .stream()
        .map(ja -> Artifact.builder().type(artifactType).link(ja.getUrl().toString()).build())
        .collect(Collectors.toCollection(() -> result));
  }
}
