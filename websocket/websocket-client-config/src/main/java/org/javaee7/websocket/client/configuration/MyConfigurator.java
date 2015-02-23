package org.javaee7.websocket.client.configuration;

import java.util.List;
import java.util.Map;
import javax.websocket.ClientEndpointConfig;
import javax.websocket.HandshakeResponse;

/**
 * @author Arun Gupta
 */
public class MyConfigurator extends ClientEndpointConfig.Configurator {

    @Override
    public void beforeRequest(Map<String, List<String>> headers) {
        System.out.println("beforeRequest:");
        for (String h : headers.keySet()) {
            for (String k : headers.get(h)) {
                System.out.println("Header: " + h + ", " + k);
            }
        }

    }

    @Override
    public void afterResponse(HandshakeResponse response) {
        System.out.println("afterResponse:");
        for (String h : response.getHeaders().keySet()) {
            for (String k : response.getHeaders().get(h)) {
                System.out.println("Header: " + h + ", " + k);
            }
        }
    }
}
