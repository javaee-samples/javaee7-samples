package org.javaee7.websocket.endpoint.javatypes;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

/**
 * @author Arun Gupta
 */
@ServerEndpoint("/websocket-int")
public class MyEndpointInt {

    @OnMessage
    public int echoInt(int i) {
        System.out.println("echoInt");
        return i;
    }
}
