package org.javaee7.websocket.client.programmatic.configuration;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;


/**
 * @author Arun Gupta
 */
@ServerEndpoint("/websocket")
public class MyEndpoint {
    
    @OnMessage
    public String sayHello(String name) {
        System.out.println("Received message in endpoint : " + name);
        return "Hello " + name;
    }
    
}
