package org.javaee7.jaxrs.sample.filter.interceptor;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

/**
 * @author Arun Gupta
 */
public class MyClientWriterInterceptor implements WriterInterceptor {

    @Override
    public void aroundWriteTo(WriterInterceptorContext wic) throws IOException, WebApplicationException {

        System.out.println("MyClientWriterInterceptor");
        wic.setOutputStream(new FilterOutputStream(wic.getOutputStream()) {

            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            
            @Override
            public void write(int b) throws IOException {
                baos.write(b);
                super.write(b);
            }

            @Override
            public void close() throws IOException {
                System.out.println("MyClientWriterInterceptor --> " + baos.toString());
                super.close();
            }
        });
        
//        wic.setOutputStream(new FilterOutputStream(wic.getOutputStream()) {
//            
//            @Override
//            public void write(int b) throws IOException {
//                System.out.println("**** "  + (char)b);
//                super.write(b);
//            }
//            
//        });
                
        wic.proceed();
    }

}
