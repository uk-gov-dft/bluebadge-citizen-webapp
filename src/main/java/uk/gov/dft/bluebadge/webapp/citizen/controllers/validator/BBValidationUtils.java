package uk.gov.dft.bluebadge.webapp.citizen.controllers.validator;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

public class BBValidationUtils {

  public static void rejectIfTooLong(
      Errors errors, String field, String errorCode, int stringLength) {
    rejectIfTooLong(errors, field, errorCode, stringLength, null, null);
  }

  public static void rejectIfTooLong(
      Errors errors,
      String field,
      String errorCode,
      int stringLength,
      @Nullable Object[] errorArgs,
      @Nullable String defaultMessage) {

    Assert.notNull(errors, "Errors object must not be null");
    Object value = errors.getFieldValue(field);
    if (value instanceof String && StringUtils.hasText(value.toString())) {
      if (((String) value).length() > stringLength) {
        errors.rejectValue(field, errorCode, errorArgs, defaultMessage);
      }
    }
  }
}
