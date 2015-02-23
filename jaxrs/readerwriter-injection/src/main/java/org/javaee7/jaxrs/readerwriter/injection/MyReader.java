package org.javaee7.jaxrs.readerwriter.injection;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

/**
 * @author Arun Gupta
 */
@Provider
@Consumes(MyObject.MIME_TYPE)
public class MyReader implements MessageBodyReader<MyObject> {

    @Inject
    AnotherObject another;

    @Override
    public boolean isReadable(Class<?> type, Type type1, Annotation[] antns, MediaType mt) {
        another.setValue(2);
        return MyObject.class.isAssignableFrom(type);
    }

    @Override
    public MyObject readFrom(Class<MyObject> type,
        Type type1,
        Annotation[] antns,
        MediaType mt, MultivaluedMap<String, String> mm,
        InputStream in) throws IOException, WebApplicationException {
        try {
            ObjectInputStream ois = new ObjectInputStream(in);
            MyObject mo = (MyObject) ois.readObject();
            mo.setIndex(another.getValue());
            return mo;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MyReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
