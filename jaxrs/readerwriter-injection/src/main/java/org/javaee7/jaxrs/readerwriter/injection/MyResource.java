package org.javaee7.jaxrs.readerwriter.injection;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * @author Arun Gupta
 */
@Path("fruits")
public class MyResource {
    private final String[] RESPONSE = { "apple", "banana", "mango" };

    @POST
    @Consumes(MyObject.MIME_TYPE)
    public String postWithCustomMimeType(MyObject mo) {
        System.out.println("endpoint invoked (getFruit(" + mo.getIndex() + "))");

        return RESPONSE[Integer.valueOf(mo.getIndex()) % 3];
    }

    @POST
    @Path("index")
    @Consumes("text/plain")
    public String postSimple(int index) {
        return RESPONSE[index % 3];
    }
}
