package uk.gov.dft.bluebadge.webapp.citizen.controllers.validator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BBValidationUtils {
  public static final String NOT_BLANK = "NotBlank";
  public static final String SIZE = "Size";
  public static final String NOT_NULL = "NotNull";

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
    if (value instanceof String && ((String) value).length() > stringLength) {
      errors.rejectValue(field, errorCode, errorArgs, defaultMessage);
    }
  }
}
