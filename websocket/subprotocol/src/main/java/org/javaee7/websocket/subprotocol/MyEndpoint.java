package org.javaee7.websocket.subprotocol;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;


/**
 * @author Arun Gupta
 */
@ServerEndpoint(value="/endpoint", 
        subprotocols="myProtocol")
public class MyEndpoint {
    @OnMessage
    public String echoText(String text) {
        return text;
    }
}
