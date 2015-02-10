package org.javaee7.servlet.resource.packaging;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
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

    @Deployment(testable = false)
    public static WebArchive deploy() throws URISyntaxException {
        return ShrinkWrap.create(WebArchive.class)
            .addAsLibrary(new File("src/main/webapp/WEB-INF/lib/myResources.jar"), "myResources.jar");
    }

    @ArquillianResource
    private URL base;

    @Test
    public void getMyResourceJarStyles() throws MalformedURLException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(URI.create(new URL(base, "styles.css").toExternalForm()));
        Response response = target.request().get();

        assertThat(response.getStatus(), is(equalTo(200)));

        String style = response.readEntity(String.class);
        assertThat(style, startsWith("body {"));
    }

}
