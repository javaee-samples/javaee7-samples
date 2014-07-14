package org.javaee7.websocket.injection;

import javax.inject.Inject;
import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

/**
 * @author Arun Gupta
 */
@ServerEndpoint("/websocket-ejb")
public class MyEndpointWithEJB {
    
    @Inject MySessionBean bean;
    
    @OnMessage
    public String sayHello(String name) {
        return bean.sayHello(name + " (from EJB)");
    }
}
