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
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This tests demonstrates the usage of a <code>permissions.xml</code> file inside
 * an ear which contains both a web module and an EJB module.
 *
 * @author Arjan Tijms
 *
 */
@RunWith(Arquillian.class)
@FixMethodOrder(NAME_ASCENDING)
public class PermissionsXMLEarTest {

    private static final String WEBAPP_SRC = "src/main/webapp";

    @ArquillianResource
    private URL base;

    @Deployment
    public static Archive<?> deploy() {
        if (System.getProperty("skipEAR") != null) {
            return create(WebArchive.class);
        }

        return
            // EAR module
            create(EnterpriseArchive.class, "appperms.ear")

                // Add permissions.xml, which is the main file we're testing
                .addAsResource("META-INF/permissions.xml")
                .setApplicationXML("META-INF/application.xml")

                // EJB module
                .addAsModule(
                    create(JavaArchive.class, "apppermsEJB.jar")

                        // Java classes containing the actual permission tests
                        // They are in the EJB module so we test the permissions work there
                        .addPackage(BeanRoot.class.getPackage())
                )

                // Web module
                .addAsModule(
                    create(WebArchive.class, "apppermsWeb.war")
                        .addAsWebInfResource((new File(WEBAPP_SRC + "/WEB-INF", "web.xml")))

                        // This class kicks off the EJB tests, but also contains tests of its own.
                        // These own tests are there to test if the permissions also work in a web module
                        .addClass(TestServlet.class)
                );
    }


    @Test
    @RunAsClient
    public void test1Startup() throws IOException, URISyntaxException {
        if (System.getProperty("skipEAR") != null) {
            return;
        }

        System.out.println("Testing Servlet from war from ear deployed at " + new URL(base, "test").toExternalForm());

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
        if (System.getProperty("skipEAR") != null) {
            return;
        }

        System.out.println("Running actual permissions.xml test");

        Response response =
                newClient()
                     .target(new URL(base, "test").toURI())
                     .queryParam("tc", "InjectLookup")
                     .request(TEXT_PLAIN)
                     .get();

        assertTrue(response.readEntity(String.class).contains("Test:Pass"));
    }

}