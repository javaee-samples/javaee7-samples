package org.javaee7.jaxrs.interceptor;

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
    public String getResource() {
        System.out.println("endpoint invoked (getResource)");
        return "banana";
    }

    @POST
    @Consumes(value = "*/*")
    @Produces("text/plain")
    public String getResource2(String index) {
        System.out.println("endpoint invoked (getResource2(" + index + "))");

        return "apple";
    }
}
