// Copyright [2019] [Payara Foundation and/or its affiliates]
package org.javaee7.jaxrs.ejb.lookup.iface.war;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.TEXT_PLAIN)
@Stateless
@Path("simplest")
public class SimplestBean {

    @Path("greet")
    @GET
    public String greet() {
        return "Hello and good bye!";
    }
}
