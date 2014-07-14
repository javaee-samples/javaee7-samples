package org.javaee7.websocket.encoder;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

/**
 * @author Arun Gupta
 */
@ServerEndpoint(value = "/encoder", 
        encoders = {MyMessageEncoder.class}, 
        decoders = {MyMessageDecoder.class})
public class MyEndpoint {
    @OnMessage
    public MyMessage messageReceived(MyMessage message) {
        System.out.println("messageReceived: " + message);
        
        return message;
    }
}
