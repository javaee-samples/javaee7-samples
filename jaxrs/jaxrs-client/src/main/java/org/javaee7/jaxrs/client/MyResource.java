package org.javaee7.jaxrs.client;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
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
@Path("persons")
@RequestScoped
public class MyResource {
    // Ideally this state should be stored in a database
    @EJB
    PersonSessionBean bean;

    @GET
    @Produces({ "application/xml", "application/json" })
    public Person[] getList() {
        return bean.getPersons().toArray(new Person[0]);
    }

    @GET
    @Produces({ "application/json", "application/xml" })
    @Path("{id}")
    public Person getPerson(@PathParam("id") int id) {
        if (id < bean.getPersons().size())
            return bean.getPersons().get(id);
        else
            return null;
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void addToList(@FormParam("name") String name, @FormParam("age") int age) {
        System.out.println("Creating a new item: " + name);
        bean.addPerson(new Person(name, age));
    }

    @PUT
    public void putToList(@FormParam("name") String name, @FormParam("age") int age) {
        addToList(name, age);
    }

    @DELETE
    @Path("{name}")
    public void deleteFromList(@PathParam("name") String name) {
        bean.deletePerson(name);
    }
}
