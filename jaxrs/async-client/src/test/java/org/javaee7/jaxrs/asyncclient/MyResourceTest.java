package org.javaee7.jaxrs.asyncclient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * In this sample we're going to explore how to communicate with a +JAX-RS+
 * service via an asynchronous invocation from the client.
 * 
 * First step; we need a service to invoke.
 * 
 * Let's create a simple +GET+ method.
 * 
 * include::MyResource#getList[]
 * 
 * For +JAX-RS+ to expose our service we need to provide an implementation of
 * the +JAX-RS+ +Application+ class to define our root path.
 * 
 * include::MyApplication[]
 */
@RunWith(Arquillian.class)
public class MyResourceTest {

    /**
     * Since +JAX-RS+ webservices are, well, web related and require a
     * web context, they are required to be deployed within a +web archive+.
     * By default, +JAX-RS+ will perform autodiscovery of our services.
     * That means there is no need to add a +web.xml+ in this scenario.
     * 
     * Based on the definition of our +@Deployment+ method, we will be
     * creating and deploying the following archive structure.
     * [source,file]
     * ----
     * /WEB-INF/
     * /WEB-INF/classes/
     * /WEB-INF/classes/org/
     * /WEB-INF/classes/org/javaee7/jaxrs/
     * /WEB-INF/classes/org/javaee7/jaxrs/asyncclient/
     * /WEB-INF/classes/org/javaee7/jaxrs/asyncclient/MyResource.class
     * /WEB-INF/classes/org/javaee7/jaxrs/asyncclient/MyApplication.class
     * ----
     */
    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
            .addClasses(MyApplication.class, MyResource.class);
    }

    @ArquillianResource
    private URL base;

    private static WebTarget target;

    @Before
    public void setUpClass() throws MalformedURLException {
        Client client = ClientBuilder.newClient();
        target = client.target(URI.create(new URL(base, "webresources/fruits").toExternalForm()));
    }

    /**
     * Before we can invoke our service we need to setup the client.
     * 
     * include::MyResourceTest#setUpClass[]
     *
     * Now we are free to invoke our deployed service by using the +JAX-RS+
     * client library.
     * 
     * The asynchronous client library comes with multiple option on how
     * to invoke the methods. First let's look at using the +Future+ option
     * with access to the complete +Response+. 
     */
    @Test
    public void testPollingResponse() throws InterruptedException, ExecutionException {
        Future<Response> r1 = target.request().async().get(); // <1> Build an asynchronous request handler for the +Response+ object
        String response = r1.get().readEntity(String.class); // <2> Read the entity from the body of the +Response+
        assertEquals("apple", response); // <3> Validate we got the expected value
    }

    /**
     * Another possibility is to use the +Future+ option with access to only the +Response+ body. 
     */
    @Test
    public void testPollingString() throws InterruptedException, ExecutionException {
        Future<String> r1 = target.request().async().get(String.class); // <1> Build an asynchronous request handler for the body of the +Response+
        String response = r1.get(); // <2> Read the entity directly from the +Future+
        assertEquals("apple", response); // <3> Validate we got the expected value
    }

    /**
     * You can also register a +InvocationCallback+ and get a callback when the +Request+ is done.
     */
    @Test
    public void testInvocationCallback() throws InterruptedException, ExecutionException {
        target.request().async().get(new InvocationCallback<String>() { // <1> Build an asynchronous request callback for the body of the +Response+

                @Override
                public void completed(String r) { // <2> Called when the +Request+ is completed and our entiy parsed
                    assertEquals("apple", r);
                }

                @Override
                public void failed(Throwable t) { // <3> Called if the +Request+ failed to complete
                    fail(t.getMessage());
                }

            });
    }

}
