package org.javaee7.websocket.endpoint.programmatic;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnOpen;
import javax.websocket.Session;

/**
 * @author Arun Gupta
 */
@ClientEndpoint
public class MyEndpointTextClient {
    public static CountDownLatch latch= new CountDownLatch(2);
    public static byte[] response;

    @OnOpen
    public void onOpen(Session session) {
        try {
            latch.countDown();
            session.getBasicRemote().sendText("Hello world!");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
