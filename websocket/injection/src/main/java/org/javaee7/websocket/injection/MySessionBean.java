package org.javaee7.websocket.injection;

import javax.ejb.Stateless;

/**
 * @author Arun Gupta
 */
@Stateless
public class MySessionBean {
    public String sayHello(String name) {
        return "Hello " + name;
    }

}
