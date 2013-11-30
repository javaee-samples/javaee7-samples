package org.javaee7.websocket.encoder.programmatic;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.ClientEndpoint;
import javax.websocket.EncodeException;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

/**
 * @author Arun Gupta
 */
@ClientEndpoint(encoders = {MyMessageEncoder.class},
        decoders={MyMessageDecoder.class})
public class MyClient {
    public static CountDownLatch latch= new CountDownLatch(4);
    
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connected to endpoint: " + session.getBasicRemote());
        try {
            MyMessage message = new MyMessage("{\"apple\" : \"red\", \"banana\": \"yellow\"}");
            session.getBasicRemote().sendObject(message);
        } catch (IOException | EncodeException ex) {
            Logger.getLogger(MyClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @OnMessage
    public MyMessage processMessage(MyMessage message) {
        latch.countDown();
        return message;
    }
    
    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }
}
