package org.javaee7.jaxrs.asyncclient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * @author Arun Gupta
 */
@Path("fruits")
public class MyResource {
    private final String[] response = { "apple", "banana", "mango" };

    @GET
    public String getList() {
        return response[0];
    }

}
