package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.validator.FutureCompoundDate;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.validator.ValidCompoundDate;
import uk.gov.dft.bluebadge.webapp.citizen.model.Journey;
import uk.gov.dft.bluebadge.webapp.citizen.model.component.CompoundDate;

@Data
@Builder
public class ProveBenefitForm implements StepForm, Serializable {

  @NotNull private Boolean hasProof;

  @ValidCompoundDate(message = "{ConditionalNotNull.awardEndDate}", mandatory = false)
  @FutureCompoundDate(message = "{NotFuture.awardEndDate}")
  private CompoundDate awardEndDate;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.PROVE_BENEFIT;
  }

  @Override
  public boolean preserveStep(Journey journey) {
    return false;
  }
}
