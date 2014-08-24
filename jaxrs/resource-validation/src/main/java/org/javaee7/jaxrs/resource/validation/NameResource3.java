package org.javaee7.jaxrs.resource.validation;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * @author Arun Gupta
 */
@Path("/names")
@NotNullAndNonEmptyNames
public class NameResource3 {

    @FormParam("firstName")
    private String firstName;
    
    @FormParam("lastName")
    private String lastName;
    
    private String email;

    @FormParam("email")
    public void setEmail(@Email String email) {
        this.email = email;
    }

//    @Email
    public String getEmail() {
        return email;
    }
    
    @POST
    @Consumes("application/x-www-form-urlencoded")
    public String registerUser() {
        return firstName + " " + lastName + " with email " + email + " registered";
    }
    
}
