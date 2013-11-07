/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.javaee7.jaxrs.asyncclient;

import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Arun Gupta
 */
public class MyResourceTest {
    
//    @ArquillianResource URL baseURL;
    
    private static WebTarget target;

    @BeforeClass
    public static void setUpClass() {
        Client client = ClientBuilder.newClient();
        target = client.target("http://localhost:8080/async-client/webresources/fruits");
    }

    /**
     * Test of getList method, of class MyResource.
     */
    @Test
    public void testPollingResponse() throws InterruptedException, ExecutionException {
        Future<Response> r1 = target.request().async().get();
        String response = r1.get().readEntity(String.class);
        assertEquals("apple", response);
    }

    /**
     * Test of getList method, of class MyResource.
     */
    @Test
    public void testPollingString() throws InterruptedException, ExecutionException {
        Future<String> r1 = target.request().async().get(String.class);
        String response = r1.get();
        assertEquals("apple", response);
    }

    /**
     * Test of getList method, of class MyResource.
     */
    @Test
    public void testInvocationCallback() throws InterruptedException, ExecutionException {
            target.request().async().get(new InvocationCallback<String>() {

                @Override
                public void completed(String r) {
                    assertEquals("apple", r);
                }

                @Override
                public void failed(Throwable t) {
                    fail(t.getMessage());
                }
                
            });
    }

}
