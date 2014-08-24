package org.javaee7.jaxrs.readerwriter.json;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.json.Json;
import javax.json.stream.JsonParser;
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
@Consumes(MediaType.APPLICATION_JSON)
public class MyReader implements MessageBodyReader<MyObject> {

    @Override
    public boolean isReadable(Class<?> type, Type type1, Annotation[] antns, MediaType mt) {
        return MyObject.class.isAssignableFrom(type);
    }

    @Override
    public MyObject readFrom(Class<MyObject> type,
            Type type1,
            Annotation[] antns,
            MediaType mt, MultivaluedMap<String, String> mm,
            InputStream in) throws IOException, WebApplicationException {
        MyObject mo = new MyObject();
        JsonParser parser = Json.createParser(in);
        while (parser.hasNext()) {
            switch (parser.next()) {
                case KEY_NAME:
                    String key = parser.getString();
                    parser.next();
                    switch (key) {
                        case "name":
                            mo.setName(parser.getString());
                            break;
                        case "age":
                            mo.setAge(parser.getInt());
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
        return mo;
    }
}
