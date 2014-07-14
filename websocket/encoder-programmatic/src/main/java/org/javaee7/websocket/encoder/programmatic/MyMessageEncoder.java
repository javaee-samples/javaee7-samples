package org.javaee7.websocket.encoder.programmatic;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * @author Arun Gupta
 */
public class MyMessageEncoder implements Encoder.Text<MyMessage> {
    @Override
    public String encode(MyMessage myMessage) throws EncodeException {
        MyClient.latch.countDown();
        return myMessage.getJsonObject().toString();
    }
    
    @Override
    public void init(EndpointConfig ec) { }

    @Override
    public void destroy() { }    
}
