package org.javaee7.websocket.endpoint;

import java.io.IOException;
import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

/**
 * @author Arun Gupta
 */
@ServerEndpoint("/bytearray")
public class MyEndpointByteArray {
    @OnMessage
    public byte[] echoBinary(byte[] data) throws IOException {
        return data;
    }
}
