package org.javaee7.jaxrs.serversentevent;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * @author Arun Gupta
 */
@ApplicationPath("webresources")
public class MyApplication extends ResourceConfig {
    public MyApplication() {
        super(MyResource.class, SseFeature.class);
    }
}
