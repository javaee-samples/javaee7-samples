package org.javaee7.websocket.binary;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * @author Arun Gupta
 */
@ServerEndpoint("/inputstream")
public class MyEndpointInputStream {

    @OnMessage
    public void echoStream(InputStream stream, Session session) throws IOException {
        System.out.println("echoStream: " + stream);
        byte[] b = new byte[12];
        stream.read(b);
        session.getBasicRemote().sendBinary(ByteBuffer.wrap(b));
    }
}
