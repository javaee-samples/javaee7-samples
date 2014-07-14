package org.javaee7.websocket.endpoint.security;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

/**
 * @author Arun Gupta
 */
@ServerEndpoint(value="/websocket")
public class MyEndpoint {
    
    @OnMessage
    public String echoText(String text) {
        return text;
    }
}
