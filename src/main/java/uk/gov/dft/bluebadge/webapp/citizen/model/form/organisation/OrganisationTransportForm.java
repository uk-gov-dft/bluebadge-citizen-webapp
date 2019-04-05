package uk.gov.dft.bluebadge.webapp.citizen.model.form.organisation;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.ORGANISATION_MAY_BE_ELIGIBLE;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.ORGANISATION_NOT_ELIGIBLE;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.ORGANISATION_TRANSPORT;

import java.io.Serializable;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Data
@Builder
public class OrganisationTransportForm implements StepForm, Serializable {

  @NotNull(message = "{organisationTransport.page.validation.select}")
  private Boolean doesTransport;

  public StepDefinition getAssociatedStep() {
    return ORGANISATION_TRANSPORT;
  }

  @Override
  public Optional<StepDefinition> determineNextStep(Journey journey) {
    if (null == doesTransport) {
      throw new IllegalStateException(
          "You must select if organisation does transport or not blue badge customers");
    }

    return doesTransport
        ? Optional.of(ORGANISATION_MAY_BE_ELIGIBLE)
        : Optional.of(ORGANISATION_NOT_ELIGIBLE);
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return false;
  }
}
