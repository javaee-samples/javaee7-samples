package org.javaee7.jaxrs.resource.validation;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * @author Arun Gupta
 */
@Path(NameAddResource.PATH)
public class NameAddResource {

    static final String PATH = "/nameadd";

    @POST
    @Consumes("application/json")
    public String addUser(@Valid Name name) {
        return name.getFirstName() + " " + name.getLastName() + " with email " + name.getEmail() + " added";
    }

}
