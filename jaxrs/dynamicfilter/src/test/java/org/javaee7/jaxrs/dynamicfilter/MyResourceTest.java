/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.javaee7.jaxrs.dynamicfilter;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Arun Gupta
 */
public class MyResourceTest {

    private static WebTarget target;

    public MyResourceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        Client client = ClientBuilder.newClient();
        target = client.target("http://localhost:8080/dynamicfilter/webresources/fruits");
    }

    @Test
    public void testGetList() {
        String result = target.request().get(String.class);
        assertEquals("apple", result);
    }

    @Test
    public void testPost() {
        String response = target.request().post(Entity.text("apple"), String.class);
        assertEquals("apple", response);
    }
}
