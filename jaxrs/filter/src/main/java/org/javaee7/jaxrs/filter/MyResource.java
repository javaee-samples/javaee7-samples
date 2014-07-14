package org.javaee7.jaxrs.filter;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

/**
 * @author Arun Gupta
 */
@Path("fruits")
public class MyResource {

    @Context
    HttpHeaders headers;

    @GET
    public String getFruit() {
        String clientHeaderValue = headers.getHeaderString("clientHeader");
        String serverHeaderValue = headers.getHeaderString("serverHeader");

        if (clientHeaderValue != null
                && clientHeaderValue.equals("clientHeaderValue")
                && serverHeaderValue != null
                && serverHeaderValue.equals("serverHeaderValue")) {
            return "apple";
        } else {
            return "banana";
        }
    }

    @POST
    @Consumes(value = "*/*")
    @Produces("text/plain")
    public String echoFruit(String fruit) {
        String clientHeaderValue = headers.getHeaderString("clientHeader");
        String serverHeaderValue = headers.getHeaderString("serverHeader");

        if (clientHeaderValue != null
                && clientHeaderValue.equals("clientHeaderValue")
                && serverHeaderValue != null
                && serverHeaderValue.equals("serverHeaderValue")) {
            return fruit;
        } else {
            return fruit.toUpperCase();
        }
    }
}
