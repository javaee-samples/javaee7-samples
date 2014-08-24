package org.javaee7.jaxrs.resource.validation;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * @author Arun Gupta
 */
@Path("/names2")
public class NameResource2 {

    @POST
    @Consumes("application/x-www-form-urlencoded")
    public String registerUser(@NotNull @FormParam("firstName") String firstName,
            @NotNull @FormParam("lastName") String lastName,
            @Email @FormParam("email") String email) {
        return firstName + " " + lastName + " with email " + email + " registered";
    }

}
