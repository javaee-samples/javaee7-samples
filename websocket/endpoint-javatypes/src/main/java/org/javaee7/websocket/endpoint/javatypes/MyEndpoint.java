package org.javaee7.websocket.endpoint.javatypes;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

/**
 * @author Arun Gupta
 */
@ServerEndpoint("/websocket")
public class MyEndpoint {

    @OnMessage
    public String echoText(String text) {
        System.out.println("echoText");
        return text;
    }
}
