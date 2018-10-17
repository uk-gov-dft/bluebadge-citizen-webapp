package uk.gov.dft.bluebadge.webapp.citizen.model.form.organisation;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.ORGANISATION_CARE;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.ORGANISATION_NOT_ELIGIBLE;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.ORGANISATION_TRANSPORT;

import java.io.Serializable;
import java.util.Optional;
import javax.validation.constraints.AssertTrue;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;

@Data
@Builder
public class OrganisationCareForm implements StepForm, Serializable {

  @AssertTrue(message = "{organisationCare.page.validation.select}")
  private Boolean doesCare;

  @Override
  public StepDefinition getAssociatedStep() {
    return ORGANISATION_CARE;
  }

  @Override
  public Optional<StepDefinition> determineNextStep() {
    return doesCare ? Optional.of(ORGANISATION_TRANSPORT) : Optional.of(ORGANISATION_NOT_ELIGIBLE);
  }
}
