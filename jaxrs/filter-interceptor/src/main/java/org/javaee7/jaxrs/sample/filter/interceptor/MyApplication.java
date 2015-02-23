package org.javaee7.jaxrs.sample.filter.interceptor;

import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * @author Arun Gupta
 */
@ApplicationPath("webresources")
public class MyApplication extends Application {

    // Resource and filters need to be explicitly specified
    // until http://java.net/jira/browse/JERSEY-1634 is fixed

    //    @Override
    //    public Set<Class<?>> getClasses() {
    //        Set<Class<?>> resources = new java.util.HashSet<>();
    //        resources.add(org.sample.filter.MyResource.class);
    //        resources.add(org.sample.filter.ServerLoggingFilter.class);
    //        return resources;
    //    }
}
