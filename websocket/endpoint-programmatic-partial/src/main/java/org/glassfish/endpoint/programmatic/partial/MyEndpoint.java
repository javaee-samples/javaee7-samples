package org.glassfish.endpoint.programmatic.partial;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

/**
 * @author Arun Gupta
 */
public class MyEndpoint extends Endpoint {

    @Override
    public void onOpen(final Session session, EndpointConfig ec) {
        session.addMessageHandler(new MessageHandler.Partial<String>() {

            @Override
            public void onMessage(String text, boolean b) {
                System.out.println("boolean(text) " + b);
                System.out.println("text length " + text.length());
                try {
                    session.getBasicRemote().sendText(text);
                } catch (IOException ex) {
                    Logger.getLogger(MyEndpoint.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        session.addMessageHandler(new MessageHandler.Partial<ByteBuffer>() {

            @Override
            public void onMessage(ByteBuffer t, boolean b) {
                System.out.println("boolean(binary) " + b);
                System.out.println("binary length " + t.array().length);
                try {
                    session.getBasicRemote().sendBinary(t);
                } catch (IOException ex) {
                    Logger.getLogger(MyEndpoint.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Closing: " + closeReason.getReasonPhrase());
    }

    @Override
    public void onError(Session session, Throwable t) {
        System.out.println("Error: " + t.getLocalizedMessage());
    }
}
