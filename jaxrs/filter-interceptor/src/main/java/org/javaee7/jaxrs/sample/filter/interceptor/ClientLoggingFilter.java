package org.javaee7.jaxrs.sample.filter.interceptor;

import java.io.IOException;
import java.util.Map.Entry;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;

/**
 * @author Arun Gupta
 */
public class ClientLoggingFilter implements ClientRequestFilter, ClientResponseFilter {

    @Override
    public void filter(ClientRequestContext crc) throws IOException {
        System.out.println("<start>ClientRequestFilter");
        System.out.println(crc.getMethod() + " " + crc.getUri());
        for (Entry e : crc.getHeaders().entrySet()) {
            System.out.print(e.getKey() + ": " + e.getValue());
        }
        System.out.println("<end>ClientRequestFilter");
    }

    @Override
    public void filter(ClientRequestContext crc, ClientResponseContext crc1) throws IOException {
        System.out.println("<start>ClientResponseFilter");
        for (Entry e : crc1.getHeaders().entrySet()) {
            System.out.print(e.getKey() + ": " + e.getValue());
        }
        System.out.println("<end>ClientResponseFilter");
    }
}
