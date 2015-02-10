package org.javaee7.jaxrs.resource.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Arun Gupta
 */
@Documented
@Target({ ElementType.ANNOTATION_TYPE,
    ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
@Size(min = 1, message = "{org.javaee7.jaxrs.resource_validation.min_size}")
@NotNull(message = "{org.javaee7.jaxrs.resource_validation.cannot_be_null}")
public @interface NotNullAndNonEmptyNames {

    String message() default "{org.javaee7.jaxrs.resource_validation.non_null_non_empty_names}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
