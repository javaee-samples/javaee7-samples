package org.javaee7.validation.custom.constraint;

import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Arun Gupta
 */
public class ZipCodeValidator
        implements ConstraintValidator<ZipCode, String> {

    List<String> zipcodes;

    @Override
    public void initialize(ZipCode constraintAnnotation) {
        System.out.println("ZipCodeValidator.initialize");
        zipcodes = new ArrayList<>();
        switch (constraintAnnotation.country()) {
            case US:
                zipcodes.add("95054");
                zipcodes.add("95051");
                zipcodes.add("94043");
                break;
            case CANADA:
                zipcodes.add("C1A");
                zipcodes.add("M3A");
                zipcodes.add("T4H");
                break;
            case MEXICO:
                zipcodes.add("01020");
                zipcodes.add("08400");
                zipcodes.add("13270");
                break;
            case INDIA:
                zipcodes.add("110092");
                zipcodes.add("400053");
                zipcodes.add("700073");
                break;
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        System.out.println("Validating: " + value);
        System.out.println("state: "+ zipcodes.contains(value));
        return zipcodes.contains(value);
    }
}
