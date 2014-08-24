package org.javaee7.jaxrs.resource.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Arun Gupta
 */
public class EmailValidator
        implements ConstraintValidator<Email, String> {

    @Override
    public void initialize(Email constraintAnnotation) {
        System.out.println("EmailValidator.initialize");
    }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        System.out.println("EmailValidator.isValid: " + value);
        return value != null && value.contains("@") && value.contains(".com");
    }
}
