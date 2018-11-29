package uk.gov.dft.bluebadge.webapp.citizen.appbuilder;

import java.util.ArrayList;
import java.util.List;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Artifact;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.ArtifactType;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.JourneyArtifact;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.ProveIdentityForm;

class ArtifactConverter {

  private ArtifactConverter() {}

  static List<Artifact> convert(Journey journey) {
    ProveIdentityForm proveIdentityForm = journey.getFormForStep(StepDefinition.PROVE_IDENTITY);
    JourneyArtifact journeyArtifact = proveIdentityForm.getJourneyArtifact();
    List<Artifact> result = new ArrayList<>();

    if (null != journeyArtifact) {
      result.add(
          Artifact.builder()
              .type(ArtifactType.PROOF_ID)
              .link(journeyArtifact.getUrl().toString())
              .build());
    }

    return result;
  }
}