package org.javaee7.servlet.security.deny.uncovered;

import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import java.io.File;
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
import org.junit.BeforeClass;
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
        WebArchive war = ShrinkWrap.create(WebArchive.class)
            .addClass(SecureServlet.class)
            .addAsResource(new File("src/main/resources/log4j.properties"))
            .addAsWebInfResource((new File("src/main/webapp/WEB-INF/web.xml")));

        System.out.println(war.toString(true));
        return war;
    }

    @BeforeClass
    public static void beforeSetup() {
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "debug");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "debug");
    }

    @Before
    public void setup() {
        correctCreds.addCredentials("u1", "p1");
        incorrectCreds.addCredentials("random", "random");
        webClient = new WebClient();
    }

    @Test
    public void testGetMethod() throws Exception {
        webClient.setCredentialsProvider(correctCreds);
        TextPage page = webClient.getPage(base + "/SecureServlet");
        assertEquals("my GET", page.getContent());
    }

    @Test
    public void testPostMethod() throws Exception {
        webClient.setCredentialsProvider(correctCreds);
        WebRequest request = new WebRequest(new URL(base + "SecureServlet"), HttpMethod.POST);
        try {
            TextPage p = webClient.getPage(request);
            System.out.println(p.getContent());
        } catch (FailingHttpStatusCodeException e) {
            assertNotNull(e);
            assertEquals(403, e.getStatusCode());
            return;
        }
        fail("POST method could be called even with deny-unocvered-http-methods");
    }

    @Test
    public void testPutMethod() throws Exception {
        webClient.setCredentialsProvider(correctCreds);
        WebRequest request = new WebRequest(new URL(base + "SecureServlet"), HttpMethod.PUT);
        try {
            TextPage p = webClient.getPage(request);
            System.out.println(p.getContent());
        } catch (FailingHttpStatusCodeException e) {
            assertNotNull(e);
            assertEquals(403, e.getStatusCode());
            return;
        }
        fail("PUT method could be called even with deny-unocvered-http-methods");
    }
}
