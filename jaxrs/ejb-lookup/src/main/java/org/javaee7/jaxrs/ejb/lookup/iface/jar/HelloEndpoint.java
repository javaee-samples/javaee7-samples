// Copyright [2019] [Payara Foundation and/or its affiliates]
package org.javaee7.jaxrs.ejb.lookup.iface.jar;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

public interface HelloEndpoint {

    @POST
    @Path("/logHello")
    void sayHelloToLog();

}
