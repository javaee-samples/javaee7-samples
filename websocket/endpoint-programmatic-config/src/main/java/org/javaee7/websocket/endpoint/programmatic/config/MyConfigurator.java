package org.javaee7.websocket.endpoint.programmatic.config;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.Extension;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * @author Arun Gupta
 */
public class MyConfigurator extends ServerEndpointConfig.Configurator {

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        System.out.println(endpointClass.getName());
        try {
            return endpointClass.newInstance();
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MyConfigurator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        System.out.println("modifyHandshake: " + request.getQueryString());
        super.modifyHandshake(sec, request, response);
    }

    @Override
    public boolean checkOrigin(String originHeaderValue) {
        System.out.println("checkOrigin: " + originHeaderValue);
        return super.checkOrigin(originHeaderValue);
    }

    @Override
    public List<Extension> getNegotiatedExtensions(List<Extension> installed, List<Extension> requested) {
        System.out.println("getNegotiatedExtensions");
        return super.getNegotiatedExtensions(installed, requested);
    }

    @Override
    public String getNegotiatedSubprotocol(List<String> supported, List<String> requested) {
        System.out.println("getNegotiatedSubprotocool");
        return super.getNegotiatedSubprotocol(supported, requested);
    }

    
}
