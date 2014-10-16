package org.javaee7.websocket.googledocs.client;

import java.io.IOException;
import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

/**
 * @author Arun Gupta
 */
@ClientEndpoint
public class MyClient {

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connected to endpoint: " + session.getBasicRemote());
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.println("Received message in client: " + message);
        for (Session peer : session.getOpenSessions()) {
            if (!peer.equals(session)) {
                peer.getBasicRemote().sendText(message);
            }
        }
    }
}
