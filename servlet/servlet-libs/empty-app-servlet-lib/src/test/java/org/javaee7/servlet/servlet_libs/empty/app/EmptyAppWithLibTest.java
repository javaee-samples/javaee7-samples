package org.javaee7.servlet.servlet_libs.empty.app;

import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.ws.rs.client.ClientBuilder;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author Arjan Tijms
 *
 */
@RunWith(Arquillian.class)
public class EmptyAppWithLibTest {
    
    @ArquillianResource
    private URL base;

    @Deployment(testable = false)
    public static WebArchive deploy() throws URISyntaxException {
        JavaArchive[] archiveWithServlet =
            Maven.resolver()
                 .loadPomFromFile("pom.xml")
                 .resolve("org.javaee7:servlet-lib")
                 .withoutTransitivity()
                 .as(JavaArchive.class);
        
        return ShrinkWrap.create(WebArchive.class)
                         .addAsLibraries(archiveWithServlet);
    }

    @Test
    @RunAsClient
    public void invokeBasicServlet() throws MalformedURLException, URISyntaxException {
        String response = 
            ClientBuilder.newClient()
                         .target(new URL(base, "basic").toURI())
                         .request()
                         .get()
                         .readEntity(String.class);
        
        assertTrue(response.startsWith("get request"));
    }

}
