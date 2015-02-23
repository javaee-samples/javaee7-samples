package org.javaee7.websocket.encoder;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

/**
 * @author Arun Gupta
 *
 */
@ClientEndpoint
public class MyEndpointClientJSONObject {
    public static CountDownLatch latch = new CountDownLatch(1);
    public static String JSON = "{\"apple\" : \"red\", \"banana\": \"yellow\"}";
    public static String response;

    @OnOpen
    public void onOpen(Session session) {
        try {
            session.getBasicRemote().sendText(JSON);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @OnMessage
    public void processMessage(String message) {
        response = message;
        latch.countDown();
    }
}
