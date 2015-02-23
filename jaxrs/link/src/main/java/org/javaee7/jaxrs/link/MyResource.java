package org.javaee7.jaxrs.link;

import java.net.URI;
import java.net.URISyntaxException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;

/**
 * @author Arun Gupta
 */
@Path("fruits")
public class MyResource {

    private final String[] response = { "apple", "banana", "mango" };

    @GET
    public String getList() {
        System.out.println("endpoint invoked");
        return response[0];
    }

    @Path("link")
    @GET
    public Response get() throws URISyntaxException {
        return Response.ok().
            link("http://oracle.com", "parent").
            link(new URI("http://jersey.java.net"), "framework").
            links(
                Link.fromUri("test1").rel("test1").build(),
                Link.fromUri("test2").rel("test2").build(),
                Link.fromUri("test3").rel("test3").build()).build();
    }
}
