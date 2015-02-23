package org.javaee7.websocket.endpoint.programmatic.async;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;

/**
 * @author Arun Gupta
 */
public class MyApplicationConfig implements ServerApplicationConfig {

    @Override
    public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> set) {
        return new HashSet<ServerEndpointConfig>() {
            {
                add(ServerEndpointConfig.Builder.create(MyEndpointHandler.class, "/websocket-handler").build());
                add(ServerEndpointConfig.Builder.create(MyEndpointFuture.class, "/websocket-future").build());
            }
        };
    }

    @Override
    public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> set) {
        return Collections.emptySet();
    }

}
