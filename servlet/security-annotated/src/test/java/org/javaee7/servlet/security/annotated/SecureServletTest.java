package org.javaee7.servlet.security.annotated;

import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.net.URL;
import javax.ws.rs.HttpMethod;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.runner.RunWith;

/**
 * @author Arun Gupta
 */
@RunWith(Arquillian.class)
public class SecureServletTest {

    @ArquillianResource
    private URL base;

    DefaultCredentialsProvider correctCreds = new DefaultCredentialsProvider();
    DefaultCredentialsProvider incorrectCreds = new DefaultCredentialsProvider();
    WebClient webClient;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class).
            addClass(SecureServlet.class);
        return war;
    }

    @Before
    public void setup() {
        correctCreds.addCredentials("u1", "p1");
        incorrectCreds.addCredentials("random", "random");
        webClient = new WebClient();
    }

    @Test
    public void testGetWithCorrectCredentials() throws Exception {
        webClient.setCredentialsProvider(correctCreds);
        HtmlPage page = webClient.getPage(base + "/SecureServlet");
        assertEquals("Servlet Security Annotated - Basic Auth with File-base Realm", page.getTitleText());
    }

    @Test
    public void testGetWithIncorrectCredentials() throws Exception {
        webClient.setCredentialsProvider(incorrectCreds);
        try {
            webClient.getPage(base + "/SecureServlet");
        } catch (FailingHttpStatusCodeException e) {
            assertNotNull(e);
            assertEquals(401, e.getStatusCode());
            return;
        }
        fail("/SecureServlet could be accessed without proper security credentials");
    }

    @Test
    public void testPostWithCorrectCredentials() throws Exception {
        webClient.setCredentialsProvider(correctCreds);
        WebRequest request = new WebRequest(new URL(base + "SecureServlet"), HttpMethod.POST);
        HtmlPage page = webClient.getPage(request);
        assertEquals("Servlet Security Annotated - Basic Auth with File-base Realm", page.getTitleText());
    }

    @Test
    public void testPostWithIncorrectCredentials() throws Exception {
        webClient.setCredentialsProvider(correctCreds);
        WebRequest request = new WebRequest(new URL(base + "SecureServlet"), HttpMethod.POST);
        try {
            webClient.getPage(request);
        } catch (FailingHttpStatusCodeException e) {
            assertNotNull(e);
            assertEquals(403, e.getStatusCode());
        }
        fail("/SecureServlet could be accessed without proper security credentials");
    }

}
