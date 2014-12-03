package org.javaee7.websocket.client.programmatic;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;


/**
 * @author Arun Gupta
 */
@ServerEndpoint(value="/websocket")
public class MyEndpoint {
    
    @OnMessage
    public String sayHello(String name, Session session) {
        System.out.println("Received message in endpoint : " + name);
        try {
            session.getBasicRemote().sendText("hello");
            session.getBasicRemote().sendText("howdy");
        } catch (IOException ex) {
            Logger.getLogger(MyEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Hello " + name;
    }
    
}
