/** Copyright Payara Services Limited **/
package org.javaee7.jaxws.endpoint.ejb;

import static javax.jws.soap.SOAPBinding.Style.RPC;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 *
 * @author Fermin Gallego
 * @author Arjan Tijms
 *
 */
@WebService
@SOAPBinding(style = RPC)
public interface EBookStore {

    @WebMethod
    String welcomeMessage(String name);
}
