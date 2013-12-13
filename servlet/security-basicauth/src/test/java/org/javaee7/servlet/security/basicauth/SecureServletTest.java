package org.javaee7.servlet.security.basicauth;

import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
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
        correctCreds.addCredentials("u1", "p1");
        incorrectCreds.addCredentials("random", "random");
    }
        
    @Test
    public void testWithCorrectCredentials() throws Exception {
        WebClient webClient = new WebClient();
        webClient.setCredentialsProvider(correctCreds);
        HtmlPage page = webClient.getPage(base + "/SecureServlet");
        assertEquals("Servlet Security - Basic Auth with File-base Realm", page.getTitleText());
    }

    @Test
    public void testWithIncorrectCredentials() throws Exception {
        WebClient webClient = new WebClient();
        webClient.setCredentialsProvider(incorrectCreds);
        try {
            webClient.getPage(base + "/SecureServlet");
        } catch(FailingHttpStatusCodeException e) {
            assertNotNull(e);
            assertEquals(401, e.getStatusCode());
            return;
        }
        fail("/SecureServlet could be accessed without proper security credentials");
    }
}
