/** Copyright Payara Services Limited **/

package org.javaee7.jaxrpc.security;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The mandated interface for a JAX-RPC remote web service.
 * 
 * <p>
 * Note the mandated extension from the {@link Remote} interface
 * and the service method having to throw a {@link RemoteException}.
 * 
 * @author Arjan Tijms
 * 
 */
public interface HelloService extends Remote {
    String sayHello(String input) throws RemoteException;
}