/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.javaee7.jaxrs.filter;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Arun Gupta
 */
public class MyResourceTest {

    private static WebTarget target;

    @BeforeClass
    public static void setUpClass() {
        Client client = ClientBuilder.newClient();
        client.register(ClientLoggingFilter.class);
        target = client.target("http://localhost:8080/filter/webresources/fruits");
    }

    /**
     * Test of getFruit method, of class MyResource.
     */
    @Test
    public void testGetFruit() {
        String result = target.request().get(String.class);
        assertEquals("Likely that the headers set in the filter were not available in endpoint",
                "apple",
                result);
    }

    /**
     * Test of getFruit2 method, of class MyResource.
     */
    @Test
    public void testPostFruit() {
        String result = target.request().post(Entity.text("apple"), String.class);
        assertEquals("Likely that the headers set in the filter were not available in endpoint",
                "apple",
                result);
    }

}
