package uk.gov.dft.bluebadge.webapp.citizen.controllers.validator;

import java.time.LocalDate;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import uk.gov.dft.bluebadge.webapp.citizen.model.component.CompoundDate;

@SuppressWarnings("squid:S2201")
public class ValidCompoundDateValidator
    implements ConstraintValidator<ValidCompoundDate, CompoundDate> {

  boolean mandatory;
  ValidCompoundDate constraintAnnotation;

  @Override
  public void initialize(ValidCompoundDate constraintAnnotation) {
    this.constraintAnnotation = constraintAnnotation;
    mandatory = constraintAnnotation.mandatory();
  }

  @Override
  public boolean isValid(CompoundDate compoundDate, ConstraintValidatorContext context) {

    if (null == compoundDate) {
      return true;
    }

    if (!compoundDate.isDatePartMissing()) {

      if (compoundDate.getYear().length() != 4) {
        return false;
      }

      try {
        LocalDate.of(
            Integer.parseInt(compoundDate.getYear()),
            Integer.parseInt(compoundDate.getMonth()),
            Integer.parseInt(compoundDate.getDay()));
      } catch (Exception e) {
        return false; // If part of the date is invalid then we cannot test
      }

      return true;
    }
    return !mandatory;
  }
}
