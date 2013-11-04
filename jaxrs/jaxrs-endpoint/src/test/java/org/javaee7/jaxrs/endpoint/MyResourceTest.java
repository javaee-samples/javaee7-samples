/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.javaee7.jaxrs.endpoint;

import java.io.File;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

/**
 * @author Arun Gupta
 */
//@RunWith(Arquillian.class)
public class MyResourceTest {

    private static WebTarget target;
    
    /**
     * Arquillian specific method for creating a file which can be deployed
     * while executing the test.
     *
     * @return a war file
     */
//    @Deployment
//    @TargetsContainer("wildfly-arquillian")
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class).
                addClass(MyApplication.class).
                addClass(Database.class).
                addClass(MyResource.class);
        System.out.println(war.toString(true));
        
        return war;
    }
    
    @BeforeClass
    public static void setupClass() {
        Client client = ClientBuilder.newClient();
        target = client.target("http://localhost:8080/jaxrs-endpoint/webresources/fruit");
    }
    
    /**
     * Test of POST method, of class MyResource.
     */
    @Test
    public void testPost() {
        System.out.println("POST");
        target.request().post(Entity.text("apple"));
        String r = target.request().get(String.class);
        assertEquals("[apple]", r);
    }

    /**
     * Test of PUT method, of class MyResource.
     */
    @Test
    public void testPut() {
        System.out.println("PUT");
        target.request().put(Entity.text("banana"));
        String r = target.request().get(String.class);
        assertEquals("[apple, banana]", r);
    }

    /**
     * Test of GET method, of class MyResource.
     */
    @Test
    public void testGetAll() {
        System.out.println("GET");
        String r = target.request().get(String.class);
        assertEquals("[apple, banana]", r);
    }

    /**
     * Test of GET method, of class MyResource.
     */
    @Test
    public void testGetOne() {
        System.out.println("GET");
        String r = target.path("apple").request().get(String.class);
        assertEquals("[apple]", r);
    }

    /**
     * Test of GET method, of class MyResource.
     */
    @Test
    public void testDelete() {
        System.out.println("DELETE");
        target.path("banana").request().delete();
        String r = target.request().get(String.class);
        assertEquals("[apple]", r);
    }

}
