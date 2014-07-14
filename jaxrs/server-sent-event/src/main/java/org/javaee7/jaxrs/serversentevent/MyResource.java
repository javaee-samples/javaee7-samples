package org.javaee7.jaxrs.serversentevent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseBroadcaster;
import org.glassfish.jersey.media.sse.SseFeature;

/**
 * @author Arun Gupta
 */
@Path("items")
public class MyResource {

    private static final Queue<String> ITEMS = new ConcurrentLinkedQueue<>();
    private static final SseBroadcaster BROADCASTER = new SseBroadcaster();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String listItems() {
        return ITEMS.toString();
    }

    @GET
    @Path("events")
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    public EventOutput itemEvents() {
        final EventOutput eventOutput = new EventOutput();
        BROADCASTER.add(eventOutput);
        return eventOutput;
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void addItem(@FormParam("name") String name) {
        ITEMS.add(name);
        // Broadcasting an un-named event with the name of the newly added item in data
        BROADCASTER.broadcast(new OutboundEvent.Builder().data(String.class, name).build());
        // Broadcasting a named "add" event with the current size of the items collection in data
        BROADCASTER.broadcast(new OutboundEvent.Builder().name("size").data(Integer.class, ITEMS.size()).build());
    }
}
