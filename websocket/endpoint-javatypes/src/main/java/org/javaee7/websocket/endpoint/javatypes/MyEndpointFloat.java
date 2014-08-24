package org.javaee7.websocket.endpoint.javatypes;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

/**
 * @author Arun Gupta
 */
@ServerEndpoint("/websocket-float")
public class MyEndpointFloat {
    
    @OnMessage
    public float echoFloat(Float f) {
        System.out.println("echoFloat");
        return f;
    }
}
