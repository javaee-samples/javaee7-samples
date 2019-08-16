// Copyright [2019] [Payara Foundation and/or its affiliates]
package org.javaee7.jaxrs.ejb.lookup.iface.war;

import java.rmi.RemoteException;

import javax.ejb.EJBMetaData;
import javax.ejb.Handle;
import javax.ejb.HomeHandle;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * javax.ejb.* classes are not allowed as business interfaces, but the bean still can be mapped.
 *
 * @author David Matejcek
 */
@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.TEXT_PLAIN)
@Stateless
@Path("illegal-interface")
public class IllegalInterfaceBean implements javax.ejb.EJBHome {

    @Override
    public void remove(Handle handle) throws RemoteException, RemoveException {
    }


    @Override
    public void remove(Object primaryKey) throws RemoteException, RemoveException {
    }


    @Path("ejbMetaData")
    @GET
    @Override
    public EJBMetaData getEJBMetaData() throws RemoteException {
        return null;
    }


    @Override
    public HomeHandle getHomeHandle() throws RemoteException {
        return null;
    }
}
