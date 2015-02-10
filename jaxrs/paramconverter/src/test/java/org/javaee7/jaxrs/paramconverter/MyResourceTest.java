package org.javaee7.jaxrs.paramconverter;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

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
 * @author Arun Gupta
 * @author Xavier Coulon
 */
@RunWith(Arquillian.class)
public class MyResourceTest {

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
            .addClasses(MyApplication.class, MyResource.class, MyBeanConverterProvider.class, MyBean.class);
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
    public void testRequestWithQueryParam() {
        String r = target.queryParam("search", "foo").request().get(String.class);
        assertEquals("foo", r);
    }

    @Test
    public void testRequestWithNoQueryParam() {
        String r = target.request().get(String.class);
        assertEquals("bar", r);
    }

    @Test
    public void testRequestWithPathParam() {
        String r = target.path("/foo").request().get(String.class);
        assertEquals("foo", r);
    }

}
