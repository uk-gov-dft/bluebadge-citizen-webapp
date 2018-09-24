package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

@Data
@Builder
public class YourIssuingAuthorityForm implements StepForm {
  String localAuthorityDescription;
  String localAuthorityShortCode;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.YOUR_ISSUING_AUTHORITY;
  }
}
