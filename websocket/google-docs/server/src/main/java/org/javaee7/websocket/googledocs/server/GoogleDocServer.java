package org.javaee7.websocket.googledocs.server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.EncodeException;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * @author Arun Gupta
 */
@ServerEndpoint(value = "/websocket")
public class GoogleDocServer {

    private static final Logger LOGGER = Logger.getLogger(GoogleDocServer.class.getName());

    @OnOpen
    public void onOpen(Session client) {
        LOGGER.log(Level.INFO, "connected");
    }

    @OnMessage
    public void broadcastText(String text, Session session) throws IOException, EncodeException {
        LOGGER.log(Level.INFO, "broadcastText: {0}", text);
        for (Session peer : session.getOpenSessions()) {
            if (!peer.equals(session)) {
                peer.getBasicRemote().sendText(text);
            }
        }
    }
}
