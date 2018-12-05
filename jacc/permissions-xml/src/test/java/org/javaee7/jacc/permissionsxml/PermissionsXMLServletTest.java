/** Copyright Payara Services Limited **/
package org.javaee7.jacc.permissionsxml;

import static javax.ws.rs.client.ClientBuilder.newClient;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertTrue;
import static org.junit.runners.MethodSorters.NAME_ASCENDING;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.ws.rs.core.Response;

import org.javaee7.jacc.contexts.bean.BeanRoot;
import org.javaee7.jacc.contexts.servlet.TestServlet;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This tests demonstrates the usage of a <code>permissions.xml</code> file inside
 * a standalone war
 *
 * @author Arjan Tijms
 *
 */
@RunWith(Arquillian.class)
@FixMethodOrder(NAME_ASCENDING)
public class PermissionsXMLServletTest {

    private static final String WEBAPP_SRC = "src/main/webapp";

    @ArquillianResource
    private URL base;

    @Deployment
    public static Archive<?> deploy() {

        return
            create(WebArchive.class)
            // Add permissions.xml, which is the main file we're testing
            .addAsResource("META-INF/permissions.xml")
            .addAsWebInfResource((new File(WEBAPP_SRC + "/WEB-INF", "web.xml")))

            // This class kicks off the EJB tests (which reside with the web module),
            // but also contains tests of its own
            .addClass(TestServlet.class)
            .addPackage(BeanRoot.class.getPackage())
            ;
    }


    @Test
    @RunAsClient
    public void test1Startup() throws IOException, URISyntaxException {
        System.out.println("Testing Servlet from war deployed at " + new URL(base, "test"));

        Response response =
                newClient()
                     .target(new URL(base, "test").toURI())
                     .queryParam("tc", "Startup")
                     .request(TEXT_PLAIN)
                     .get();

        assertTrue(response.readEntity(String.class).contains("Test:Pass"));
    }

    @Test
    @RunAsClient
    public void test2PermissionsXML() throws IOException, URISyntaxException {
        System.out.println("Running actual permissions.xml test");

        Response response =
                newClient()
                     .target(new URL(base, "test").toURI())
                     .queryParam("tc", "InjectLookup")
                     .queryParam("web", "true")
                     .request(TEXT_PLAIN)
                     .get();

        assertTrue(response.readEntity(String.class).contains("Test:Pass"));
    }

}