package org.javaee7.websocket.endpoint;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

/**
 * @author Arun Gupta
 */
@ServerEndpoint("/text")
public class MyEndpointText {
    
    @OnMessage
    public String echoText(String name) {
        return name;
    }
}
