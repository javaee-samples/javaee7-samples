package org.javaee7.websocket.client.configuration;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;


/**
 * @author Arun Gupta
 */
@ServerEndpoint(value="/websocket")
public class MyEndpoint {
    
    @OnMessage
    public String sayHello(String name) {
        System.out.println("Received message in endpoint : " + name);
        return "Hello " + name;
    }
    
}
