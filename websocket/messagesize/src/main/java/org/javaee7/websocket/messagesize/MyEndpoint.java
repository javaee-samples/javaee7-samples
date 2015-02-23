package org.javaee7.websocket.messagesize;

import java.io.IOException;
import java.nio.ByteBuffer;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

/**
 * @author Arun Gupta
 */
@ServerEndpoint(value = "/websocket")
public class MyEndpoint {

    @OnMessage(maxMessageSize = 6)
    public String echoText(String data) {
        return data;
    }

    @OnMessage(maxMessageSize = 6)
    public ByteBuffer echoBinary(ByteBuffer data) throws IOException {
        return data;
    }

    @OnClose
    public void onClose(CloseReason reason) {
        System.out.println("CLOSED: " + reason.getCloseCode() + ", " + reason.getReasonPhrase());
    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }
}
