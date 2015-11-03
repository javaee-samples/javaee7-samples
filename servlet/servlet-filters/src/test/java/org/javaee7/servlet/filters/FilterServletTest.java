package org.javaee7.servlet.filters;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import static org.hamcrest.CoreMatchers.*;

@RunWith(Arquillian.class)
public class FilterServletTest {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
            .addClass(CharResponseWrapper.class)
            .addClasses(TestServlet.class, FooBarFilter.class);
    }

    @ArquillianResource
    private URL base;

    @Test
    @RunAsClient
    public void filtered_servlet_should_return_enhanced_foobar_text() throws MalformedURLException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(URI.create(new URL(base, "filtered/TestServlet").toExternalForm()));

        Response response = target.request().get();
        Assert.assertThat(response.readEntity(String.class), is(equalTo("foo--bar--bar")));
    }

    @Test
    @RunAsClient
    public void standard_servlet_should_return_simple_text() throws MalformedURLException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(URI.create(new URL(base, "TestServlet").toExternalForm()));

        Response response = target.request().get();
        Assert.assertThat(response.readEntity(String.class), is(equalTo("bar")));
    }
}
