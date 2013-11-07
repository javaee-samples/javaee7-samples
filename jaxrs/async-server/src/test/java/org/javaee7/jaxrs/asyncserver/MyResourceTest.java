/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.javaee7.jaxrs.asyncserver;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
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
        target = client.target("http://localhost:8080/async-server/webresources/fruits");
    }
    
    /**
     * Test of getList method, of class MyResource.
     */
    @Test
    public void testGetList() {
        String result = target.request().get(String.class);
        assertEquals("apple", result);
    }
    
}
