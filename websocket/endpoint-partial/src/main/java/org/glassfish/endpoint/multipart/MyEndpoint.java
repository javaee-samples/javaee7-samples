package org.glassfish.endpoint.multipart;

import java.io.IOException;
import java.nio.ByteBuffer;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * @author Arun Gupta
 */
@ServerEndpoint(value="/websocket")
public class MyEndpoint {
    
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connected from: " + session.getRequestURI());
    }
    
    @OnMessage
//    public String echoText(String data) {
    public String echoText(String data, boolean part) {
        System.out.println("boolean(text) " + data);
        System.out.println("text length " + data.length());
        if (part) {
            System.out.println("whole message received");
        } else {
            System.out.println("partial message received");
        }
        return data;
    }

    @OnMessage
    public void echoBinary(ByteBuffer data, Session session) throws IOException {
//    public ByteBuffer echoBinary(ByteBuffer data, boolean part) {
        System.out.println("boolean(binary) " + data);
        System.out.println("binary length " + data.array().length);
//        if (part) {
//            System.out.println("whole message received");
//        } else {
//            System.out.println("partial message received");
//        }
        session.getBasicRemote().sendBinary(data);
//        return data;
    }
}
