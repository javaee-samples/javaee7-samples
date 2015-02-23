package org.javaee7.jaxrs.client;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

/**
 * @author Arun Gupta
 */
@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MyResourceTest {

    private WebTarget target;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
            .addClasses(
                MyApplication.class, MyResource.class, People.class,
                Person.class, PersonSessionBean.class);
    }

    @ArquillianResource
    private URL base;

    @Before
    public void setUp() throws MalformedURLException {
        Client client = ClientBuilder.newClient();
        target = client.target(URI.create(new URL(base, "webresources/persons").toExternalForm()));
        target.register(Person.class);
    }

    /**
     * Test of getList method, of class MyResource.
     */
    @Test
    public void test1PostAndGet() {
        MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();
        map.add("name", "Penny");
        map.add("age", "1");
        target.request().post(Entity.form(map));

        map.clear();
        map.add("name", "Leonard");
        map.add("age", "2");
        target.request().post(Entity.form(map));

        map.clear();
        map.add("name", "Sheldon");
        map.add("age", "3");
        target.request().post(Entity.form(map));

        Person[] list = target.request().get(Person[].class);
        assertEquals(3, list.length);

        assertEquals("Penny", list[0].getName());
        assertEquals(1, list[0].getAge());

        assertEquals("Leonard", list[1].getName());
        assertEquals(2, list[1].getAge());

        assertEquals("Sheldon", list[2].getName());
        assertEquals(3, list[2].getAge());
    }

    /**
     * Test of getPerson method, of class MyResource.
     */
    @Test
    public void test2GetSingle() {
        Person p = target
            .path("{id}")
            .resolveTemplate("id", "1")
            .request(MediaType.APPLICATION_XML)
            .get(Person.class);
        assertEquals("Leonard", p.getName());
        assertEquals(2, p.getAge());
    }

    /**
     * Test of putToList method, of class MyResource.
     */
    @Test
    public void test3Put() {
        MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();
        map.add("name", "Howard");
        map.add("age", "4");
        target.request().post(Entity.form(map));

        Person[] list = target.request().get(Person[].class);
        assertEquals(4, list.length);

        assertEquals("Howard", list[3].getName());
        assertEquals(4, list[3].getAge());
    }

    /**
     * Test of deleteFromList method, of class MyResource.
     */
    @Test
    public void test4Delete() {
        target
            .path("{name}")
            .resolveTemplate("name", "Howard")
            .request()
            .delete();
        Person[] list = target.request().get(Person[].class);
        assertEquals(3, list.length);
    }

    @Test
    public void test5ClientSideNegotiation() {
        String json = target.request().accept(MediaType.APPLICATION_JSON).get(String.class);

        JsonReader reader = Json.createReader(new StringReader(json));
        JsonArray jsonArray = reader.readArray();
        assertEquals(1, jsonArray.getJsonObject(0).getInt("age"));
        assertEquals("Penny", jsonArray.getJsonObject(0).getString("name"));
        assertEquals(2, jsonArray.getJsonObject(1).getInt("age"));
        assertEquals("Leonard", jsonArray.getJsonObject(1).getString("name"));
        assertEquals(3, jsonArray.getJsonObject(2).getInt("age"));
        assertEquals("Sheldon", jsonArray.getJsonObject(2).getString("name"));
    }

    @Test
    public void test6DeleteAll() {
        Person[] list = target.request().get(Person[].class);
        for (Person p : list) {
            target
                .path("{name}")
                .resolveTemplate("name", p.getName())
                .request()
                .delete();
        }
        list = target.request().get(Person[].class);
        assertEquals(0, list.length);
    }

}
