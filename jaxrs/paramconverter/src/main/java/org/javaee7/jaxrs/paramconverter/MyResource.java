package org.javaee7.jaxrs.paramconverter;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * @author Arun Gupta
 * @author Xavier coulon
 */
@Path("/endpoint")
public class MyResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getWithQuery(@DefaultValue("bar") @QueryParam("search") MyBean myBean) {
        return myBean.getValue();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getByPath(@PathParam("id") MyBean myBean) {
        return myBean.getValue();
    }
}
