/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.javaee7.jaxrs.asyncclient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Arun Gupta
 */
@RunWith(Arquillian.class)
public class MyResourceTest {
    
    @Deployment(testable = false)
    public static WebArchive createDeployment() {
       return ShrinkWrap.create(WebArchive.class)
             .addClasses(MyApplication.class, MyResource.class);
    }

    @ArquillianResource
    private URL base;
    
    private static WebTarget target;

    @Before
    public void setUpClass() throws MalformedURLException {
        Client client = ClientBuilder.newClient();
        target = client.target(URI.create(new URL(base, "webresources/fruits").toExternalForm()));
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
