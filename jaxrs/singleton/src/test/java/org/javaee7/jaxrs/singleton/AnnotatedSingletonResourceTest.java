package org.javaee7.jaxrs.singleton;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.StringTokenizer;

import static org.junit.Assert.assertEquals;

/**
 * @author Arun Gupta
 */
@RunWith(Arquillian.class)
public class AnnotatedSingletonResourceTest {
    @ArquillianResource
    private URL base;

    private Client client;
    private WebTarget target;

    @Before
    public void setUp() throws MalformedURLException {
        client = ClientBuilder.newClient();
        target = client.target(URI.create(new URL(base, "webresources/annotated").toExternalForm()));
    }

    @After
    public void tearDown() {
        client.close();
    }

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
            .addClasses(
                MyAnnotatedApplication.class,
                AnnotatedSingletonResource.class);
    }

    @Test
    @InSequence(1)
    public void testPost() {
        target.request().post(Entity.text("pineapple"));
        target.request().post(Entity.text("mango"));
        target.request().post(Entity.text("kiwi"));
        target.request().post(Entity.text("passion fruit"));

        String list = target.request().get(String.class);
        System.out.println("--> " + list);
        StringTokenizer tokens = new StringTokenizer(list, ",");
        assertEquals(4, tokens.countTokens());
    }

    @Test
    @InSequence(2)
    public void testGet() {
        String response = target.path("2").request().get(String.class);
        assertEquals("kiwi", response);
    }

    @Test
    @InSequence(3)
    public void testDelete() {
        target.path("kiwi").request().delete();

        String list = target.request().get(String.class);
        StringTokenizer tokens = new StringTokenizer(list, ",");
        assertEquals(3, tokens.countTokens());
    }

    @Test
    @InSequence(4)
    public void testPut() {
        target.request().put(Entity.text("apple"));

        String list = target.request().get(String.class);
        StringTokenizer tokens = new StringTokenizer(list, ",");
        assertEquals(4, tokens.countTokens());
    }
}
