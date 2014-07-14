package org.javaee7.websocket.endpoint;

import java.io.IOException;
import java.nio.ByteBuffer;
import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

/**
 * @author Arun Gupta
 */
@ServerEndpoint("/bytebuffer")
public class MyEndpointByteBuffer {
    @OnMessage
    public ByteBuffer echoBinary(ByteBuffer data) throws IOException {
        System.out.println("echoBinary");
        return data;
    }
}
