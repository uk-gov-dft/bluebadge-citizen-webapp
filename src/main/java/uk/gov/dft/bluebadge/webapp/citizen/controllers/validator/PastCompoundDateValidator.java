package uk.gov.dft.bluebadge.webapp.citizen.controllers.validator;

import java.time.LocalDate;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import uk.gov.dft.bluebadge.webapp.citizen.model.component.CompoundDate;

public class PastCompoundDateValidator
    implements ConstraintValidator<PastCompoundDate, CompoundDate> {

  @Override
  public boolean isValid(CompoundDate compoundDate, ConstraintValidatorContext context) {

    if (compoundDate == null) {
      return true;
    }

    // Effectively means we cannot validate this date as it's an incomplete date
    if (compoundDate.isDatePartMissing()) {
      return true;
    }

    // Attempt to parse and compare
    try {
      // "compoundDate.getYear().length() > 4" is added to avoid having two validations (this and ValidCompoundDate happening at the same time).
      return compoundDate.getYear().length() > 4
          || compoundDate.getLocalDate().isBefore(LocalDate.now());
    } catch (Exception e) {
      return true; // If part of the date is invalid then we cannot test
    }
  }
}
