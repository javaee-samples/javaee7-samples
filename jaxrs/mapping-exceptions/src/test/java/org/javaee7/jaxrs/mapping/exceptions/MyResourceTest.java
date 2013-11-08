/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.javaee7.jaxrs.mapping.exceptions;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author argupta
 */
public class MyResourceTest {
    WebTarget target;
    
    @Before
    public void setUp() {
        Client client = ClientBuilder.newClient();
        target = client
                .target("http://localhost:8080/mapping-exceptions/webresources/order");
    }

    /**
     * Test of getOrder method, of class MyResource.
     */
    @Test
    public void testOddOrder() {
        String response = target.path("1").request().get(String.class);
        assertEquals("1", response);
    }
    
    /**
     * Test of getOrder method, of class MyResource.
     */
    @Test
    public void testEvenOrder() {
        try {
            System.out.print(target.path("2").request().get(String.class));
        } catch (ClientErrorException e) {
            assertEquals(412, e.getResponse().getStatus());
        } catch (Exception e) {
            fail(e.getMessage());
        }
            
    }
    
}
