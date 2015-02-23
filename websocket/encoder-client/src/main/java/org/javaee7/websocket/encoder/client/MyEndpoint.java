package org.javaee7.websocket.encoder.client;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

/**
 * @author Arun Gupta
 */
@ServerEndpoint("/encoder-client")
public class MyEndpoint {

    @OnOpen
    public void onOpen() {
    }

    @OnMessage
    public String echoText(String text) {
        return text;
    }

}
