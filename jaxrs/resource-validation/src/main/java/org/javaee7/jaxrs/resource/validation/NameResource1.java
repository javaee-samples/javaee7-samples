package org.javaee7.jaxrs.resource.validation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * @author Arun Gupta
 */
@Path("/names1")
public class NameResource1 {

    @NotNull
    @Size(min = 1)
    @FormParam("firstName")
    private String firstName;
    
    @NotNull
    @Size(min = 1)
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
