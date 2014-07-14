package org.javaee7.websocket.client.programmatic;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.Session;

/**
 * @author Arun Gupta
 */
public class MyClient extends Endpoint {
    @Override
    public void onOpen(final Session session, EndpointConfig ec) {
        session.addMessageHandler(new MessageHandler.Whole<String>() {

            @Override
            public void onMessage(String text) {
                System.out.println("Received response in client from endpoint: " + text);
            }
        });
        System.out.println("Connected to endpoint: " + session.getBasicRemote());
        try {
            String name = "Duke";
            System.out.println("Sending message from client -> endpoint: " + name);
            session.getBasicRemote().sendText(name);
        } catch (IOException ex) {
            Logger.getLogger(MyClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        super.onClose(session, closeReason);
    }

    @Override
    public void onError(Session session, Throwable t) {
        t.printStackTrace();
    }
}
