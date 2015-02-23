package org.javaee7.jaxrs.jsonp;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Arun Gupta
 */
@RunWith(Arquillian.class)
public class MyResourceTest {

    Client client;
    WebTarget targetObject;
    WebTarget targetArray;

    @ArquillianResource
    URL base;

    @Before
    public void setUp() throws MalformedURLException {
        client = ClientBuilder.newClient();
        targetObject = client.target(URI.create(new URL(base, "webresources/object").toExternalForm()));
        targetArray = client.target(URI.create(new URL(base, "webresources/array").toExternalForm()));
    }

    @After
    public void tearDown() {
        client.close();
    }

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
            .addClasses(
                MyApplication.class,
                MyObjectResource.class,
                MyArrayResource.class);
    }

    /**
     * Test of echoObject method, of class MyObjectResource.
     */
    @Test
    public void testEchoObject() {
        JsonObject jsonObject = Json.createObjectBuilder()
            .add("apple", "red")
            .add("banana", "yellow")
            .build();

        JsonObject json = targetObject
            .request()
            .post(Entity.entity(jsonObject, MediaType.APPLICATION_JSON), JsonObject.class);
        assertNotNull(json);
        assertFalse(json.isEmpty());
        assertTrue(json.containsKey("apple"));
        assertEquals("red", json.getString("apple"));
        assertTrue(json.containsKey("banana"));
        assertEquals("yellow", json.getString("banana"));
    }

    @Test
    public void testEchoArray() {
        JsonArray jsonArray = Json.createArrayBuilder()
            .add(Json.createObjectBuilder()
                .add("apple", "red"))
            .add(Json.createObjectBuilder()
                .add("banana", "yellow"))
            .build();

        JsonArray json = targetArray
            .request()
            .post(Entity.entity(jsonArray, MediaType.APPLICATION_JSON), JsonArray.class);
        assertNotNull(json);
        assertEquals(2, json.size());
        assertTrue(json.getJsonObject(0).containsKey("apple"));
        assertEquals("red", json.getJsonObject(0).getString("apple"));
        assertTrue(json.getJsonObject(1).containsKey("banana"));
        assertEquals("yellow", json.getJsonObject(1).getString("banana"));
    }

}
