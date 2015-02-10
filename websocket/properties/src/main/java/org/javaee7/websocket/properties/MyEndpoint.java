package org.javaee7.websocket.properties;

import java.util.Map;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * @author Arun Gupta
 */
@ServerEndpoint(value = "/websocket")
public class MyEndpoint {

    @OnMessage
    public String echoText(String name, Session session) {
        Map<String, Object> map = session.getUserProperties();
        Integer count = 0;
        if (map.get("count") != null) {
            count = (Integer) map.get("count");
        }
        System.out.format("Called %d times", ++count);
        map.put("count", count);
        return name + String.valueOf(count);
    }
}
