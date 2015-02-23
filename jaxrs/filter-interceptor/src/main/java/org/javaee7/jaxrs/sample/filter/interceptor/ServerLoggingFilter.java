package org.javaee7.jaxrs.sample.filter.interceptor;

import java.io.IOException;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 * By default, a filter is globally bound. That means it is applied
 * to all resources and all methods within that resource.
 * 
 * @author Arun Gupta
 */
@Provider
@Priority(Priorities.USER)
// default value
public class ServerLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext crc) throws IOException {
        System.out.println("<start>ContainerRequestFilter");
        System.out.println(crc.getMethod() + " " + crc.getUriInfo().getAbsolutePath());
        for (String key : crc.getHeaders().keySet()) {
            System.out.println(key + ": " + crc.getHeaders().get(key));
        }
        System.out.println("<end>ContainerRequestFilter");
    }

    @Override
    public void filter(ContainerRequestContext crc, ContainerResponseContext crc1) throws IOException {
        System.out.println("<start>ContainerResponseFilter");
        for (String key : crc1.getHeaders().keySet()) {
            System.out.println(key + ": " + crc1.getHeaders().get(key));
        }
        System.out.println("<end>ContainerResponseFilter");
    }
}
