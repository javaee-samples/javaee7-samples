package org.javaee7.websocket.endpoint;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

/**
 * @author Arun Gupta
 */
@ClientEndpoint
public class MyEndpointTextClient {
    public static CountDownLatch latch;

    @OnOpen
    public void onOpen(Session session) {
        try {
            session.getBasicRemote().sendText("Hello world!");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    @OnMessage
    public void processMessage(String message) {
        latch.countDown();
    }
}
