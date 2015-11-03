package org.javaee7.jaxrs.beanvalidation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;

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
        target = client.target(URI.create(new URL(base, "webresources/endpoint").toExternalForm()));
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

    @Test
    public void testValidPostRequest() {
        MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();
        map.add("name", "Penny");
        map.add("age", "1");
        target.request().post(Entity.form(map));

        map.clear();
        map.add("name", "Leonard");
        map.add("age", "2");
        target.request().post(Entity.form(map));
    }

    @Test
    public void testInvalidPostRequest() {
        try {
            MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();
            map.add("name", null);
            map.add("age", "1");
            target.request().post(Entity.form(map));
        } catch (BadRequestException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testInvalidPostRequestLesserAge() {
        try {
            MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();
            map.add("name", "Penny");
            map.add("age", "0");
            target.request().post(Entity.form(map));
        } catch (BadRequestException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testInvalidPostRequestGreaterAge() {
        try {
            MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();
            map.add("name", "Penny");
            map.add("age", "11");
            target.request().post(Entity.form(map));
        } catch (BadRequestException e) {
            assertNotNull(e);
        }
    }

}
