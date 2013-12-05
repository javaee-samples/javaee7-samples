package org.javaee7.websocket.endpoint.async;

import java.nio.ByteBuffer;
import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;

/**
 *
 * @author Jacek Jackowiak
 */
@ClientEndpoint
public class MyAsyncEndpointByteBufferClient {

    private ByteBuffer receivedMessage;

    @OnMessage
    public void onMessage(ByteBuffer msg) {
        receivedMessage = msg;
    }

    public ByteBuffer getReceivedMessage() {
        return receivedMessage;
    }
}
