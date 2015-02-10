package org.javaee7.jaxrs.singleton;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Arun Gupta
 */
@Singleton
@Path("annotated")
public class AnnotatedSingletonResource {
    // Ideally this state should be stored in a database
    // But this is a singleton resource and so state can be saved here too
    private List<String> strings;

    public AnnotatedSingletonResource() {
        strings = new ArrayList<>();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getAll() {
        return strings.toString();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("{id}")
    public String getString(@PathParam("id") int id) {
        return strings.get(id);
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public void postString(String content) {
        strings.add(content);
    }

    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    public void putToList(String content) {
        strings.add(content);
    }

    @DELETE
    @Path("{content}")
    public void deleteFromList(@PathParam("content") String content) {
        if (strings.contains(content))
            strings.remove(content);
    }
}
