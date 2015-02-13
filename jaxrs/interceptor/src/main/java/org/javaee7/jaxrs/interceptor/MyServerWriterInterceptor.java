package org.javaee7.jaxrs.interceptor;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

/**
 * @author Arun Gupta
 */
@Provider
public class MyServerWriterInterceptor implements WriterInterceptor {

    @Override
    public void aroundWriteTo(WriterInterceptorContext wic) throws IOException, WebApplicationException {
        System.out.println("MyServerWriterInterceptor");
        wic.setOutputStream(new FilterOutputStream(wic.getOutputStream()) {

            final ByteArrayOutputStream baos = new ByteArrayOutputStream();

            @Override
            public void write(int b) throws IOException {
                baos.write(b);
                super.write(b);
            }

            @Override
            public void close() throws IOException {
                System.out.println("MyServerWriterInterceptor --> " + baos.toString());
                super.close();
            }
        });

        wic.proceed();
    }

}
