package org.javaee7.jaxrs.dynamicfilter;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

/**
 * @author Arun Gupta
 */
public class ServerLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext crc) throws IOException {
        System.out.println("ContainerRequestFilter<start>");
        System.out.println(crc.getMethod() + " " + crc.getUriInfo().getAbsolutePath());
        for (String key : crc.getHeaders().keySet()) {
            System.out.println("<header> " + key + ": " + crc.getHeaders().get(key));
        }

        // add a header, check in test
        crc.getHeaders().add("myHeader", "myValue");

        System.out.println("ContainerRequestFilter<end>");
    }

    @Override
    public void filter(ContainerRequestContext crc, ContainerResponseContext crc1) throws IOException {
        System.out.println("ContainerResponseFilter<start>");
        for (String key : crc1.getHeaders().keySet()) {
            System.out.println("<header> " + key + ": " + crc1.getHeaders().get(key));
        }
        System.out.println("ContainerResponseFilter<end>");
    }
}
