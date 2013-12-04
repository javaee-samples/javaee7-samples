package org.javaee7.websocket.endpoint.async;

import javax.websocket.*;

/**
 * 
 * @author Jacek Jackowiak
 */
@ClientEndpoint
public class MyAsyncEndpointTextClient {

    private String receivedMessage;

    @OnMessage
    public void onMessage(String msg) {
        receivedMessage = msg;
    }

    public String getReceivedMessage() {
        return receivedMessage;
    }
}
