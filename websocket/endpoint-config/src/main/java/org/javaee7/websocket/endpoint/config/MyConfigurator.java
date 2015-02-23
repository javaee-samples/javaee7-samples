package org.javaee7.websocket.endpoint.config;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * @author Arun Gupta
 */
public class MyConfigurator extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        super.modifyHandshake(sec, request, response);
        System.out.println("Handshake Request:");
        System.out.println("Serving at: " + request.getRequestURI());
        System.out.println("Handshake Response:");
        for (String h : response.getHeaders().keySet()) {
            for (String k : response.getHeaders().get(h)) {
                System.out.println("Header: " + h + ", " + k);
            }
        }
    }

}
