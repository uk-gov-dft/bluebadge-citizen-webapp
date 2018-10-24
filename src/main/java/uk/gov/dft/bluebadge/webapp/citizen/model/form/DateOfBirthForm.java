package uk.gov.dft.bluebadge.webapp.citizen.model.form;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepDefinition;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.journey.StepForm;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.validator.PastCompoundDate;
import uk.gov.dft.bluebadge.webapp.citizen.controllers.validator.ValidCompoundDate;
import uk.gov.dft.bluebadge.webapp.citizen.model.component.CompoundDate;

@Data
@Builder
@EqualsAndHashCode
public class DateOfBirthForm implements StepForm, Serializable {

  @ValidCompoundDate @PastCompoundDate private CompoundDate dateOfBirth;

  @Override
  public StepDefinition getAssociatedStep() {
    return StepDefinition.DOB;
  }
}
