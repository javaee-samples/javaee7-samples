package org.javaee7.websocket.websocket.vs.rest.payload;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

/**
 * @author Arun Gupta
 */
@ServerEndpoint("/websocket")
public class MyWebSocketEndpoint {

    @OnMessage
    public String echoText(String text) {
        return text;
    }
}
