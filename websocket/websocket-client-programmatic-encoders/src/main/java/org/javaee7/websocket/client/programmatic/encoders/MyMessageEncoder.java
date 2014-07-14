package org.javaee7.websocket.client.programmatic.encoders;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * @author Arun Gupta
 */
public class MyMessageEncoder implements Encoder.Text<MyMessage> {
    @Override
    public String encode(MyMessage myMessage) throws EncodeException {
        return myMessage.getJsonObject().toString();
    }
    
    @Override
    public void init(EndpointConfig ec) {
//        System.out.println("init");
    }

    @Override
    public void destroy() {
//        System.out.println("desroy");
    }    
}
