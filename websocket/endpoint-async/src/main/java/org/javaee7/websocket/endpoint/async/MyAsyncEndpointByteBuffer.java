package org.javaee7.websocket.endpoint.async;

import java.nio.ByteBuffer;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * @author Jacek Jackowiak
 */
@ServerEndpoint("/bytebuffer")
public class MyAsyncEndpointByteBuffer {

    @OnMessage
    public void echoByteBuffer(ByteBuffer data, Session session) {
        session.getAsyncRemote().sendBinary(data);
    }
}
