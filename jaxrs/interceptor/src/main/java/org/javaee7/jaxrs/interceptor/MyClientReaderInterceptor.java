package org.javaee7.jaxrs.interceptor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;

/**
 * @author Arun Gupta
 */
public class MyClientReaderInterceptor implements ReaderInterceptor {

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext ric) throws IOException, WebApplicationException {
        
        System.out.println("MyClientReaderInterceptor");
//        ric.setInputStream(new FilterInputStream(ric.getInputStream()) {
//
//            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//            @Override
//            public int read(byte[] b, int off, int len) throws IOException {
//                baos.write(b, off, len);
////                System.out.println("@@@@@@ " + b);
//                return super.read(b, off, len);
//            }
//
//            @Override
//            public void close() throws IOException {
//                System.out.println("### " + baos.toString());
//                super.close();
//            }
//        });
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
