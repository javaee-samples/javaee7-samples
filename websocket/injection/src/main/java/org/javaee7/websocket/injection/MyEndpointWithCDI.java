package org.javaee7.websocket.injection;

import javax.inject.Inject;
import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

/**
 * @author Arun Gupta
 */
@ServerEndpoint("/websocket-cdi")
@Logging
public class MyEndpointWithCDI {
    
    @Inject MyBean bean;
    
    @OnMessage
    public String sayHello(String name) {
        System.out.println(getClass().getName() + ".sayHello");
        return bean.sayHello(name + " (from CDI)");
    }
}
