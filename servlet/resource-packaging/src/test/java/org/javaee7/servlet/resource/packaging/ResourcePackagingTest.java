package org.javaee7.servlet.resource.packaging;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Logger;

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

/**
 * @author Jakub Marchwicki
 */
@RunWith(Arquillian.class)
public class ResourcePackagingTest {
    
    Logger logger = Logger.getLogger(ResourcePackagingTest.class.getName());

    @Deployment(testable = false)
    public static WebArchive deploy() throws URISyntaxException {
        return ShrinkWrap.create(WebArchive.class)
                         .addAsLibrary(new File("src/main/webapp/WEB-INF/lib/myResources.jar"), "myResources.jar");
    }

    @ArquillianResource
    private URL base;

    @Test
    @RunAsClient
    public void getMyResourceJarStyles() throws MalformedURLException, URISyntaxException {
        Response response = 
            ClientBuilder.newClient()
                         .target(new URL(base, "styles.css").toURI())
                         .request()
                         .get();
        
        assertEquals(200, response.getStatus());

        String style = response.readEntity(String.class);
        
        assertTrue(style.startsWith("body {"));
    }

}
