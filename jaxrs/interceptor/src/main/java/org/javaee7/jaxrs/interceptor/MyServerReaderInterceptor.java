package org.javaee7.jaxrs.interceptor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;

/**
 * @author Arun Gupta
 */
@Provider
public class MyServerReaderInterceptor implements ReaderInterceptor {

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext ric) throws IOException, WebApplicationException {
        System.out.println("MyServerReaderInterceptor");
        final InputStream old = ric.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int c;
        while ((c = old.read()) != -1) {
            baos.write(c);
        }
        System.out.println("MyClientReaderInterceptor --> " + baos.toString());
        
        ric.setInputStream(new ByteArrayInputStream(baos.toByteArray()));
        
        return ric.proceed();
    }

}
