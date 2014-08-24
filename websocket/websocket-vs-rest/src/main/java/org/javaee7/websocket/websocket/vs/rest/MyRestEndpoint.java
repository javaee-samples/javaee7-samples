package org.javaee7.websocket.websocket.vs.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * REST Web Service
 *
 * @author Arun Gupta
 */
@Path("rest")
public class MyRestEndpoint {

    @POST
    @Produces("text/plain")
    public String getXml(String payload) {
        return payload;
    }
}
