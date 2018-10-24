package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

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
}
