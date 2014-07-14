package org.javaee7.websocket.encoder.client;

import java.io.StringReader;
import javax.json.Json;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

/**
 * @author Arun Gupta
 */
public class MyMessageDecoder implements Decoder.Text<MyMessage> {

    @Override
    public MyMessage decode(String string) throws DecodeException {
        MyClient.latch.countDown();
        return new MyMessage(Json.createReader(new StringReader(string)).readObject());
    }

    @Override
    public boolean willDecode(String string) {
        return true;
    }

    @Override
    public void init(EndpointConfig ec) { }

    @Override
    public void destroy() { }
}
