package org.javaee7.websocket.client.programmatic.encoders;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

/**
 * @author Arun Gupta
 */
@ServerEndpoint("/websocket")
public class MyEndpoint {

    @OnMessage
    public String echoText(String text) {
        System.out.println("Received message in endpoint : " + text);
        return text;
    }

}
