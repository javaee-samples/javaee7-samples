package org.javaee7.websocket.parameters;

import javax.websocket.OnMessage;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * @author Arun Gupta
 */
@ServerEndpoint(value="/greet/{name}")
public class GreetingBean {
    @OnMessage
    public String sayHello(String payload, @PathParam("name")String name) {
        return payload + " " + name + "!";
    }
}
