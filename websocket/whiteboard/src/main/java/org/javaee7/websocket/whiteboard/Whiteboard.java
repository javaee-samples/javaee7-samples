package org.javaee7.websocket.whiteboard;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * @author Arun Gupta
 */
@ServerEndpoint(value = "/websocket",
    encoders = { FigureEncoder.class },
    decoders = { FigureDecoder.class })
public class Whiteboard {

    private static final Logger LOGGER = Logger.getLogger(Whiteboard.class.getName());

    private static final Object PRESENT = new Object();

    private static final ConcurrentMap<Session, Object> peers = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session peer) {
        peers.put(peer, PRESENT);
    }

    @OnClose
    public void onClose(Session peer) {
        peers.remove(peer);
    }

    @OnMessage
    public void broadcastFigure(Figure figure, Session session) throws IOException, EncodeException {
        LOGGER.log(Level.INFO, "boradcastFigure: {0}", figure);
        for (Session peer : session.getOpenSessions()) {
            //        for (Session peer : peers.keySet()) {
            if (!peer.equals(session)) {
                peer.getBasicRemote().sendObject(figure);
            }
        }
    }

    @OnMessage
    public void broadcastSnapshot(ByteBuffer data, Session session) throws IOException {
        LOGGER.log(Level.INFO, "broadcastBinary: {0}", data);
        for (Session peer : session.getOpenSessions()) {
            //        for (Session peer : peers.keySet()) {
            if (!peer.equals(session)) {
                peer.getBasicRemote().sendBinary(data);
            }
        }
    }
}
