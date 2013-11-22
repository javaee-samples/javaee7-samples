/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.javaee7.jaxrs.jsonp;

import java.net.MalformedURLException;
import java.net.URL;
import javax.json.Json;
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
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

/**
 * @author Arun Gupta
 */
@RunWith(Arquillian.class)
public class MyResourceTest {

    Client client;
    WebTarget target;

    @ArquillianResource
    URL base;

    @Before
    public void setUp() throws MalformedURLException {
        client = ClientBuilder.newClient();
        target = client.target(new URL(base, "webresources/endpoint").toExternalForm());
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
                        MyResource.class);
    }

    /**
     * Test of echoObject method, of class MyResource.
     */
    @Test
    public void testEchoObject() {
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("apple", "red")
                .add("banana", "yellow")
                .build();

        JsonObject response = target
                .request()
                .post(Entity.entity(jsonObject, MediaType.APPLICATION_JSON), JsonObject.class);
        System.out.println(response);
    }

}
