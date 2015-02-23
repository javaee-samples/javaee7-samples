package org.javaee7.websocket.endpoint.singleton;

import javax.websocket.server.ServerEndpointConfig;

/**
 * @author Arun Gupta
 */
public class MyConfigurator extends ServerEndpointConfig.Configurator {

    private static final MyEndpoint ENDPOINT = new MyEndpoint();

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        if (MyEndpoint.class.equals(endpointClass)) {
            return (T) ENDPOINT;
        } else {
            throw new InstantiationException();
        }
    }

}
