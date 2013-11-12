/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.javaee7.jaxrs.beanvalidation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.net.URL;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

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
    private static WebTarget target;

    @ArquillianResource
    private URL base;

    @Before
    public void setUpClass() throws MalformedURLException {
        Client client = ClientBuilder.newClient();
        target = client.target(new URL(base, "webresources/endpoint").toExternalForm());
    }

    @Test
    public void testInvalidRequest() {
        try {
            target.request().post(Entity.text("fo"), String.class);
            fail("Request must fail with payload < 3");
        } catch (BadRequestException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testValidRequest() {
        String r = target.request().post(Entity.text("foo"), String.class);
        assertEquals("foo", r);
    }

}
