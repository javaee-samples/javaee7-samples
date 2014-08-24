package org.javaee7.websocket.endpoint.config;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

/**
 * @author Arun Gupta
 */
@ServerEndpoint(value="/websocket", configurator = MyConfigurator.class)
public class MyEndpoint {
    
    @OnMessage
    public String echoText(String name) {
        return name;
    }

}
