package by.sivko.touristbot.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueCityNameValidator.class)
@Documented
public @interface UniqueCityName {
    String message() default "This city is already exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
