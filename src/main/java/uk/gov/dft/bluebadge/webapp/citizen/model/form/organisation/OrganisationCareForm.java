package uk.gov.dft.bluebadge.webapp.citizen.model.form.organisation;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.ORGANISATION_CARE;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.ORGANISATION_NOT_ELIGIBLE;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.ORGANISATION_TRANSPORT;

import java.io.Serializable;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Data
@Builder
@EqualsAndHashCode
public class OrganisationCareForm implements StepForm, Serializable {

  @NotNull(message = "{organisationCare.page.validation.select}")
  private Boolean doesCare;

  @Override
  public StepDefinition getAssociatedStep() {
    return ORGANISATION_CARE;
  }

  @Override
  public Optional<StepDefinition> determineNextStep(Journey journey) {
    if (null == doesCare) {
      throw new IllegalStateException(
          "You must select if organisation does care or not for blue badge customers");
    }
    return doesCare ? Optional.of(ORGANISATION_TRANSPORT) : Optional.of(ORGANISATION_NOT_ELIGIBLE);
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return false;
  }
}
