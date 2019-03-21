package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.AFCS_COMPENSATION_SCHEME;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.ELIGIBLE;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.HIGHER_RATE_MOBILITY;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.MAIN_REASON;
import static uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition.PIP_MOVING_AROUND;

import java.io.Serializable;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.client.applicationmanagement.model.EligibilityCodeField;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;

@Data
@Builder
@EqualsAndHashCode
public class ReceiveBenefitsForm implements StepForm, Serializable {

  @NotNull private EligibilityCodeField benefitType;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.RECEIVE_BENEFITS;
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return false;
  }
}
