package org.javaee7.websocket.endpoint.async;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * @author Arun Gupta
 */
@ServerEndpoint("/text")
public class MyAsyncEndpointText {

    @OnMessage
    public void echoText(String text, Session session) {
        session.getAsyncRemote().sendText(text);
    }
}
