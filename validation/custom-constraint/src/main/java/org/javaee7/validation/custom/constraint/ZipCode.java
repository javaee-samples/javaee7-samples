package org.javaee7.validation.custom.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author Arun Gupta
 */
@Documented
@Target({ElementType.ANNOTATION_TYPE,
    ElementType.METHOD,
    ElementType.FIELD,
    ElementType.CONSTRUCTOR,
    ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ZipCodeValidator.class)
@Size(min = 5, message = "{org.sample.zipcode.min_size}")
@Pattern(regexp = "[0-9]*")
@NotNull(message = "{org.sample.zipcode.cannot_be_null}")
public @interface ZipCode {

    String message() default "{org.sample.zipcode.invalid_zipcode}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Country country() default Country.US;

    public enum Country {

        US,
        CANADA,
        MEXICO,
        INDIA
    }
}
