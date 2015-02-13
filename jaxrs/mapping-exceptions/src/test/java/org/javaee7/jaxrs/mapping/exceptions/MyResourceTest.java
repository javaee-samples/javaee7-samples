package org.javaee7.jaxrs.mapping.exceptions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
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
 *
 * @author argupta
 */
@RunWith(Arquillian.class)
public class MyResourceTest {

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
            .addClasses(
                MyApplication.class, MyResource.class,
                OrderNotFoundException.class, OrderNotFoundExceptionMapper.class);
    }

    @ArquillianResource
    private URL base;

    private WebTarget target;

    @Before
    public void setUp() throws MalformedURLException {
        Client client = ClientBuilder.newClient();
        target = client
            .target(URI.create(new URL(base, "webresources/order").toExternalForm()));
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
