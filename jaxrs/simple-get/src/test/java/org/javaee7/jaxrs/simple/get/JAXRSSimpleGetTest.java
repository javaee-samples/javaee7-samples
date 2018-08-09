/** Copyright Payara Services Limited **/

package org.javaee7.jaxrs.simple.get;

import static javax.ws.rs.client.ClientBuilder.newClient;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

import org.javaee7.jaxrs.simple.get.JaxRsActivator;
import org.javaee7.jaxrs.simple.get.Resource;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This sample tests one of the simplest possible JAX-RS resources; one that only
 * has a single method responding to a GET request and returning a (small) string.
 *
 * @author Arjan Tijms
 */
@RunWith(Arquillian.class)
public class JAXRSSimpleGetTest {

    @ArquillianResource
    private URL base;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        WebArchive archive =
            create(WebArchive.class)
                .addClasses(
                    JaxRsActivator.class,
                    Resource.class
                    );

        System.out.println("************************************************************");
        System.out.println(archive.toString(true));
        System.out.println("************************************************************");

        return archive;
    }

    @Test
    @RunAsClient
    public void testGet() throws IOException {

        String response =
                newClient()
                     .target(
                         URI.create(new URL(base, "rest/resource/hi").toExternalForm()))
                     .request(TEXT_PLAIN)
                     .get(String.class);

        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Response: \n\n" + response);
        System.out.println("-------------------------------------------------------------------------");

        assertTrue(
            response.contains("hi")
        );
    }



}
