package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.client.referencedata.model.LocalAuthorityRefData;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Data
@Builder
@EqualsAndHashCode
public class YourIssuingAuthorityForm implements StepForm, Serializable {
  String localAuthorityDescription;
  String localAuthorityShortCode;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.YOUR_ISSUING_AUTHORITY;
  }

  @Override
  public Optional<StepDefinition> determineNextStep(Journey journey) {
    if (journey.isApplicantOrganisation()) {
      return Optional.of(StepDefinition.ORGANISATION_CARE);
    }

    return Optional.of(StepDefinition.EXISTING_BADGE);
  }

  @Override
  public boolean preserveStep(Journey journey) {
    LocalAuthorityRefData authority = journey.getLocalAuthority();
    if(null == authority || null == authority.getShortCode()){
      return false;
    }

    return authority.getShortCode().equals(localAuthorityShortCode);
  }
}
