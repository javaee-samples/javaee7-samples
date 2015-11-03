package org.javaee7.jaxrs.endpoint;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

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
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

/**
 * @author Arun Gupta
 */
@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MyResourceTest {

    private static WebTarget target;

    /**
     * Arquillian specific method for creating a file which can be deployed
     * while executing the test.
     *
     * @return a war file
     */
    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class).
            addClass(MyApplication.class).
            addClass(Database.class).
            addClass(MyResource.class);
        System.out.println(war.toString(true));

        return war;
    }

    @ArquillianResource
    private URL base;

    @Before
    public void setupClass() throws MalformedURLException {
        Client client = ClientBuilder.newClient();
        target = client.target(URI.create(new URL(base, "webresources/fruit").toExternalForm()));
    }

    /**
     * Test of POST method, of class MyResource.
     */
    @Test
    public void test1Post() {
        target.request().post(Entity.text("apple"));
        String r = target.request().get(String.class);
        assertEquals("[apple]", r);
    }

    /**
     * Test of PUT method, of class MyResource.
     */
    @Test
    public void test2Put() {
        target.request().put(Entity.text("banana"));
        String r = target.request().get(String.class);
        assertEquals("[apple, banana]", r);
    }

    /**
     * Test of GET method, of class MyResource.
     */
    @Test
    public void test3GetAll() {
        String r = target.request().get(String.class);
        assertEquals("[apple, banana]", r);
    }

    /**
     * Test of GET method, of class MyResource.
     */
    @Test
    public void test4GetOne() {
        String r = target.path("apple").request().get(String.class);
        assertEquals("apple", r);
    }

    /**
     * Test of GET method, of class MyResource.
     */
    @Test
    public void test5Delete() {
        target.path("banana").request().delete();
        String r = target.request().get(String.class);
        assertEquals("[apple]", r);

        target.path("apple").request().delete();
        r = target.request().get(String.class);
        assertEquals("[]", r);
    }
}
