package org.javaee7.websocket.client.programmatic.encoders;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.CloseReason;
import javax.websocket.EncodeException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

/**
 * @author Arun Gupta
 */
public class MyClient extends Endpoint {
    @Override
    public void onOpen(final Session session, EndpointConfig ec) {
        session.addMessageHandler(new MessageHandler.Whole<MyMessage>() {

            @Override
            public void onMessage(MyMessage message) {
                System.out.println("Received response in client from endpoint: " + message);
            }
        });
        try {
            MyMessage message = new MyMessage("{ \"foo\" : \"bar\"}");
            System.out.println("Sending message from client -> endpoint: " + message);
            session.getBasicRemote().sendObject(message);
        } catch (EncodeException | IOException ex) {
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
