package org.javaee7.websocket.endpoint.programmatic.async;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.SendHandler;
import javax.websocket.SendResult;
import javax.websocket.Session;

/**
 * @author Arun Gupta
 */
public class MyEndpointHandler extends Endpoint {

    @Override
    public void onOpen(final Session session, EndpointConfig ec) {
        session.addMessageHandler(new MessageHandler.Whole<String>() {

            @Override
            public void onMessage(String data) {
                System.out.println("Received (MyEndpointHandler) : " + data);

                session.getAsyncRemote().sendText(data, new SendHandler() {

                    @Override
                    public void onResult(SendResult sr) {
                        if (sr.isOK()) {
                            System.out.println("Message written to the socket (handler)");
                        } else {
                            System.out.println("Message NOT written to the socket (handler)");
                            sr.getException().printStackTrace();
                        }

                    }
                });
            }
        });
    }
}
