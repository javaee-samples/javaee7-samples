package org.javaee7.jaxrs.singleton;

import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * @author Arun Gupta
 */
@ApplicationPath("webresources")
public class MyApplication extends Application {

    @Override
    public Set<Object> getSingletons() {
        Set<Object> resources = new java.util.HashSet<>();
        resources.add(new ApplicationSingletonResource());
        return resources;
    }
}
