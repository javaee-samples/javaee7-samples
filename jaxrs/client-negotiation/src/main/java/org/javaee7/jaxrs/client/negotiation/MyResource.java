package org.javaee7.jaxrs.client.negotiation;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author Arun Gupta
 */
@Path("persons")
public class MyResource {
    @GET
    @Produces({"application/xml", "application/json"})
    public List<Person> getList() {
        People people = new People();
        people.add(new Person("Penny", 1));
        people.add(new Person("Leonard", 2));
        people.add(new Person("Sheldon", 3));
        return people;
    }
}
