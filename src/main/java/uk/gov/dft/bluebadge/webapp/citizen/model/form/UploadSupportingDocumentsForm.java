package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.ARMS;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.CHILDBULK;
import static uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField.CHILDVEHIC;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.JourneyArtifact;

@Data
@Builder
public class UploadSupportingDocumentsForm implements StepForm, ArtifactForm, Serializable {

  @NotNull private Boolean hasDocuments;

  private List<JourneyArtifact> journeyArtifacts = new ArrayList<>();

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

  @Override
  public Optional<StepDefinition> determineNextStep(Journey journey) {
    if (EligibilityCodeField.WALKD.equals(journey.getEligibilityCode())) {
      return Optional.of(StepDefinition.TREATMENT_LIST);
    }
    if (journey.getEligibilityCode() == CHILDVEHIC) {
      return Optional.of(StepDefinition.HEALTHCARE_PROFESSIONAL_LIST);
    }
    if (journey.getEligibilityCode() == ARMS) {
      return Optional.of(StepDefinition.ARMS_HOW_OFTEN_DRIVE);
    }
    if (journey.getEligibilityCode() == CHILDBULK) {
      return Optional.of(StepDefinition.MEDICAL_EQUIPMENT);
    }
    throw new IllegalStateException("Failed to determine next step for current step:" + this);
  }
}
