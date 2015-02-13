package org.javaee7.jaxrs.dynamicfilter;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;

/**
 * @author Arun Gupta
 */
@Path("fruits")
public class MyResource {
    private final String[] response = { "apple", "banana", "mango" };

    @Context
    Request request;
    @Context
    HttpHeaders headers;

    @GET
    public String getList() {
        System.out.println("GET");
        System.out.println("--> size=" + headers.getRequestHeaders().keySet().size());
        for (String header : headers.getRequestHeaders().keySet()) {
            System.out.println("--> " + header);
            if (header.equals("myHeader")
                && headers.getRequestHeader(header).get(0).equals("myValue")) {
                return response[0];
            }
        }
        return response[1];
    }

    @POST
    public String echoFruit(String fruit) {
        System.out.println("POST");
        return fruit;
    }

}
