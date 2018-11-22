package uk.gov.dft.bluebadge.webapp.citizen.model.form.blind;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
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
public class RegisteredCouncilForm implements Serializable, StepForm {

  @NotBlank(message = "{NotBlank.registeredCouncil}")
  private String registeredCouncil;

  private LocalAuthorityRefData localAuthorityForRegisteredBlind;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.REGISTERED_COUNCIL;
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return false;
  }
}
