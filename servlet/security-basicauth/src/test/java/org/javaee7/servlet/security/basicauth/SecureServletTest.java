package org.javaee7.servlet.security.basicauth;

import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.File;
import java.net.URL;
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

    private static final String WEBAPP_SRC = "src/main/webapp";

    @ArquillianResource
    private URL base;

    WebClient webClient;
    DefaultCredentialsProvider correctCreds = new DefaultCredentialsProvider();
    DefaultCredentialsProvider incorrectCreds = new DefaultCredentialsProvider();

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class).
            addClass(SecureServlet.class).
            addAsWebInfResource((new File(WEBAPP_SRC + "/WEB-INF", "web.xml")));
        return war;
    }

    @Before
    public void setup() {
        webClient = new WebClient();
        correctCreds.addCredentials("u1", "p1");
        incorrectCreds.addCredentials("random", "random");
    }

    @Test
    public void testGetWithCorrectCredentials() throws Exception {
        webClient.setCredentialsProvider(correctCreds);
        TextPage page = webClient.getPage(base + "/SecureServlet");
        assertEquals("my GET", page.getContent());
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
        WebRequest request = new WebRequest(new URL(base + "/SecureServlet"), HttpMethod.POST);
        TextPage page = webClient.getPage(request);
        assertEquals("my POST", page.getContent());
    }

    @Test
    public void testPostWithIncorrectCredentials() throws Exception {
        webClient.setCredentialsProvider(incorrectCreds);
        WebRequest request = new WebRequest(new URL(base + "/SecureServlet"), HttpMethod.POST);
        try {
            webClient.getPage(request);
        } catch (FailingHttpStatusCodeException e) {
            assertNotNull(e);
            assertEquals(401, e.getStatusCode());
            return;
        }
        fail("/SecureServlet could be accessed without proper security credentials");
    }
}
