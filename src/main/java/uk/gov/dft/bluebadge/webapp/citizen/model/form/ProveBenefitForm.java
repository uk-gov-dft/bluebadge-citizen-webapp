package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.validator.DateConstraintToEnum;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.validator.ValidCompoundDate;
import uk.gov.dft.bluebadge.webapp.citizen.model.component.CompoundDate;

@Data
@Builder
public class ProveBenefitForm implements StepForm {

  @NotNull private Boolean hasProof;

  @ValidCompoundDate(mandatory = false, constraintTo = DateConstraintToEnum.FUTURE)
  private CompoundDate awardEndDate;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.PROVE_BENEFIT;
  }
}
