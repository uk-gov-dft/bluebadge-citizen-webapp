package uk.gov.dft.bluebadge.webapp.citizen.controllers.validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = PastCompoundDateValidator.class)
@Target({FIELD, METHOD})
@Retention(RUNTIME)
@Documented
public @interface PastCompoundDate {

  String message() default "{CompoundDate.cannotBeInThePast}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
