package org.javaee7.jsf.server.extension;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * @author Arun Gupta
 */
@FacesValidator("nameValidator")
public class NameValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        System.out.println("Got: " + value);
        if (((String) value).length() < 3)
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_WARN,
                "Incorrect name length",
                "Name length must >= 3, found only " + value));
    }

}
