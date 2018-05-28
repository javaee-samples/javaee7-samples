/** Copyright Payara Services Limited **/

package org.javaee7.jaxrpc.security;

/**
 * Implementation class for the JAX-RPC remote web service.
 * 
 * @author Arjan Tijms
 * 
 */
public class HelloServiceImpl implements HelloService {

    public String sayHello(String input) {
        return "Hello " + input;
    }
}