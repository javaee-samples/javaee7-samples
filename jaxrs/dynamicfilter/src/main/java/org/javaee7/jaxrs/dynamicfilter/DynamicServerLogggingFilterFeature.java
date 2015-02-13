package org.javaee7.jaxrs.dynamicfilter;

import javax.ws.rs.GET;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

/**
 * @author Arun Gupta
 */
@Provider
public class DynamicServerLogggingFilterFeature implements DynamicFeature {

    @Override
    public void configure(ResourceInfo ri, FeatureContext fc) {
        //        if (MyResource.class.isAssignableFrom(ri.getResourceClass())
        //                && ri.getResourceMethod().isAnnotationPresent(GET.class)) {
        fc.register(new ServerLoggingFilter());
        //        }
    }
}
