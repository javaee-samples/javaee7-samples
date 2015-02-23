package org.javaee7.jaxrs.sample.filter.interceptor;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author Arun Gupta
 */
@Path("fruits")
public class MyResource {
    @GET
    public String getFruit() {
        return "apple";
    }

    @POST
    @Consumes(value = "*/*")
    @Produces("text/plain")
    public String getFruit2(String index) {
        return "apple";
    }
}
