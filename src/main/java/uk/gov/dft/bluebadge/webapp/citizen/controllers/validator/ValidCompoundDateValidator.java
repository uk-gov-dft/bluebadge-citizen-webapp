package uk.gov.dft.bluebadge.webapp.citizen.controllers.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import uk.gov.dft.bluebadge.webapp.citizen.model.component.CompoundDate;

public class ValidCompoundDateValidator
    implements ConstraintValidator<ValidCompoundDate, CompoundDate> {

  @Override
  public boolean isValid(CompoundDate compoundDate, ConstraintValidatorContext context) {

    if (null == compoundDate) {
      return true;
    }

    return !compoundDate.isDatePartMissing();
  }
}
