package uk.gov.dft.bluebadge.webapp.citizen.appbuilder;

import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.ARMS;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.CHILDBULK;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.CHILDVEHIC;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.DLA;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.PIP;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.WALKD;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.Artifact;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.ArtifactType;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.form.*;

class ArtifactConverter {
  private static final EnumSet<EligibilityCodeField> BENEFIT_UPLOAD_ELIG_TYPES =
      EnumSet.of(PIP, DLA);
  private static final EnumSet<EligibilityCodeField> SUPPORT_DOCS_ELIG_TYPES =
      EnumSet.of(WALKD, ARMS, CHILDBULK, CHILDVEHIC);

  private ArtifactConverter() {}

  static List<Artifact> convert(Journey journey) {
    List<Artifact> result = new ArrayList<>();

    if (BENEFIT_UPLOAD_ELIG_TYPES.contains(journey.getEligibilityCode())) {
      UploadBenefitForm uploadBenefitForm = journey.getFormForStep(StepDefinition.UPLOAD_BENEFIT);
      convertArtifact(result, uploadBenefitForm, ArtifactType.PROOF_ELIG);
    }

    if (SUPPORT_DOCS_ELIG_TYPES.contains(journey.getEligibilityCode())) {
      UploadSupportingDocumentsForm form =
          journey.getFormForStep(StepDefinition.UPLOAD_SUPPORTING_DOCUMENTS);
      convertArtifact(result, form, ArtifactType.SUPPORT_DOCS);
    }

    ProveIdentityForm proveIdentityForm = journey.getFormForStep(StepDefinition.PROVE_IDENTITY);
    convertArtifact(result, proveIdentityForm, ArtifactType.PROOF_ID);

    ProvidePhotoForm providePhotoForm = journey.getFormForStep(StepDefinition.PROVIDE_PHOTO);
    convertArtifact(result, providePhotoForm, ArtifactType.PHOTO);

    ProveAddressForm proveAddressForm = journey.getFormForStep(StepDefinition.PROVE_ADDRESS);
    convertArtifact(result, proveAddressForm, ArtifactType.PROOF_ADD);

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
