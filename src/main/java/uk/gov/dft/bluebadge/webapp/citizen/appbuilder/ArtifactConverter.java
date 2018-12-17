package uk.gov.dft.bluebadge.webapp.citizen.appbuilder;

import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.PIP;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Artifact;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.ArtifactType;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ArtifactForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ProveIdentityForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ProvidePhotoForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.UploadBenefitForm;

class ArtifactConverter {

  private ArtifactConverter() {}

  static List<Artifact> convert(Journey journey) {
    List<Artifact> result = new ArrayList<>();

    if (PIP == journey.getEligibilityCode()) {
      UploadBenefitForm uploadBenefitForm = journey.getFormForStep(StepDefinition.UPLOAD_BENEFIT);
      convertArtifact(result, uploadBenefitForm, ArtifactType.PROOF_ELIG);
    }

    ProveIdentityForm proveIdentityForm = journey.getFormForStep(StepDefinition.PROVE_IDENTITY);
    convertArtifact(result, proveIdentityForm, ArtifactType.PROOF_ID);

    ProvidePhotoForm providePhotoForm = journey.getFormForStep(StepDefinition.PROVIDE_PHOTO);
    convertArtifact(result, providePhotoForm, ArtifactType.PHOTO);

    return result;
  }

  private static void convertArtifact(
      List<Artifact> result, ArtifactForm artifactForm, ArtifactType artifactType) {
    Assert.notNull(artifactForm, "Artifact form cannot be null");
    artifactForm
        .getJourneyArtifacts()
        .stream()
        .map(ja -> Artifact.builder().type(artifactType).link(ja.getUrl().toString()).build())
        .collect(Collectors.toCollection(() -> result));
  }
}
