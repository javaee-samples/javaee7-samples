package org.javaee7.websocket.endpoint.javatypes;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

/**
 * @author Arun Gupta
 */
@ServerEndpoint("/websocket-reader")
public class MyEndpointReader {
    
    @OnMessage
    public String echoReader(Reader reader) {
        System.out.println("echoReader");
        CharBuffer buffer = CharBuffer.allocate(20);
        try {
            reader.read(buffer);
        } catch (IOException ex) {
            Logger.getLogger(MyEndpointReader.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        return new String(buffer.array());
    }
}
