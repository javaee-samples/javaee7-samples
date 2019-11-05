package org.javaee7.servlet.filters;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class FilterServletTest {

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
            .addClasses(
                CharResponseWrapper.class, 
                TestServlet.class, 
                FooBarFilter.class);
    }

    @ArquillianResource
    private URL base;

    @Test
    @RunAsClient
    public void filtered_servlet_should_return_enhanced_foobar_text() throws MalformedURLException {
        Response response = 
            ClientBuilder.newClient()
                         .target(URI.create(new URL(base, "filtered/TestServlet").toExternalForm()))
                         .request()
                         .get();
        
        assertThat(response.readEntity(String.class), is(equalTo("foo--bar--bar")));
    }

    @Test
    @RunAsClient
    public void standard_servlet_should_return_simple_text() throws MalformedURLException {
        Response response = 
            ClientBuilder.newClient()
                         .target(URI.create(new URL(base, "TestServlet").toExternalForm()))
                         .request()
                         .get();
        
        assertThat(response.readEntity(String.class), is(equalTo("bar")));
    }
}
