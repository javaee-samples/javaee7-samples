package org.javaee7.jaxrs.filter;

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
            .addClasses(MyApplication.class, MyResource.class, ServerLoggingFilter.class);
    }

    private WebTarget target;

    @ArquillianResource
    private URL base;

    @Before
    public void setUpClass() throws MalformedURLException {
        Client client = ClientBuilder.newClient();
        client.register(ClientLoggingFilter.class);
        target = client.target(URI.create(new URL(base, "webresources/fruits").toExternalForm()));
    }

    /**
     * Test of getFruit method, of class MyResource.
     */
    @Test
    public void testGetFruit() {
        String result = target.request().get(String.class);
        assertEquals("Likely that the headers set in the filter were not available in endpoint",
            "apple",
            result);
    }

    /**
     * Test of getFruit2 method, of class MyResource.
     */
    @Test
    public void testPostFruit() {
        String result = target.request().post(Entity.text("apple"), String.class);
        assertEquals("Likely that the headers set in the filter were not available in endpoint",
            "apple",
            result);
    }

}
