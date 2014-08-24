package org.javaee7.jaxrs.beanvalidation;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Arun Gupta
 */
@Path("/endpoint")
public class MyResource {

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String post(@Size(min = 3) String payload) {
        return payload;
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void post2(@NotNull @FormParam("name")String name, @Min(1) @Max(10)@FormParam("age")int age) {
    }
}
