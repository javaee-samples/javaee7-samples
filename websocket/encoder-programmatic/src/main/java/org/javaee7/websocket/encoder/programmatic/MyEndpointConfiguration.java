package org.javaee7.websocket.encoder.programmatic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.websocket.Decoder;
import javax.websocket.Encoder;
import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;

/**
 * @author Arun Gupta
 */
public class MyEndpointConfiguration implements ServerApplicationConfig {

    List<Class<? extends Encoder>> encoders = new ArrayList<>();
    List<Class<? extends Decoder>> decoders = new ArrayList<>();

    public MyEndpointConfiguration() {
        encoders.add(MyMessageEncoder.class);
        decoders.add(MyMessageDecoder.class);
    }

    @Override
    public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> set) {
       Set<ServerEndpointConfig> config = new HashSet<ServerEndpointConfig>();
       config.add(ServerEndpointConfig.Builder.create(MyEndpoint.class, "/encoder-programmatic")
                        .encoders(encoders)
                        .decoders(decoders)
                        .build());
       return config;
    }

    @Override
    public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> set) {
        return Collections.emptySet();
    }
}
