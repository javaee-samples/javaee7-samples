// Copyright [2019] [Payara Foundation and/or its affiliates]
package org.javaee7.jaxrs.ejb.lookup.sbn.war3;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Stateless bean with a default name and default <code>@Local</code> interface.
 *
 * @author David Matejcek
 */
@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.APPLICATION_JSON)
@Stateless
@Path("same-bean-name-bean")
public class SameBeanName {

    @GET
    @Path("ok")
    public String ok() {
        return "OK3";
    }
}
