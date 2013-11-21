/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.javaee7.jaxrs.singelton.application;

import java.util.StringTokenizer;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

/**
 * @author Arun Gupta
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MyResourceTest {

    WebTarget target;
    
    @Before
    public void setUp() {
        Client client = ClientBuilder.newClient();
        target = client.target("http://localhost:8080/singleton-application/webresources/myresource");
    }

    @Test
    public void test1Post() {
        target.request().post(Entity.text("pineapple"));
        target.request().post(Entity.text("mango"));
        target.request().post(Entity.text("kiwi"));
        target.request().post(Entity.text("passion fruit"));
        
        String list = target.request().get(String.class);
        StringTokenizer tokens = new StringTokenizer(list, ",");
        assertEquals(4, tokens.countTokens());
    }

    @Test
    public void test2Get() {
        String response = target.path("2").request().get(String.class);
        assertEquals("kiwi", response);
    }

    @Test
    public void test3Delete() {
        target.path("kiwi").request().delete();
        
        String list = target.request().get(String.class);
        StringTokenizer tokens = new StringTokenizer(list, ",");
        assertEquals(3, tokens.countTokens());
    }

    @Test
    public void test4Put() {
        target.request().put(Entity.text("apple"));
        
        String list = target.request().get(String.class);
        StringTokenizer tokens = new StringTokenizer(list, ",");
        assertEquals(4, tokens.countTokens());
    }
}
