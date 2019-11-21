package org.javaee7.servlet.programmatic.login;

import static org.javaee7.ServerOperations.addUsersToContainerIdentityStore;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author Arun Gupta
 */
@RunWith(Arquillian.class)
public class LoginServletTest {

    private static final String WEBAPP_SRC = "src/main/webapp";

    @ArquillianResource
    private URL base;
    
    WebClient webClient;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        
        addUsersToContainerIdentityStore();
        
        return create(WebArchive.class).
            addClass(LoginServlet.class).
            addAsWebInfResource((new File(WEBAPP_SRC + "/WEB-INF", "web.xml")));
    }
    
    @Before
    public void setup() {
        webClient = new WebClient();
    }
    
    @After
    public void tearDown() {
        webClient.getCookieManager().clearCookies();
        webClient.close();
    }

    @Test
    public void testUnauthenticatedRequest() throws IOException, SAXException {
        HtmlPage page = webClient.getPage(base + "/LoginServlet");
        String responseText = page.asText();
        
        System.out.println("testUnauthenticatedRequest:\n" + responseText + "\n");

        assertTrue(responseText.contains("isUserInRole?false"));
        assertTrue(responseText.contains("getRemoteUser?null"));
        assertTrue(responseText.contains("getUserPrincipal?null"));
        assertTrue(responseText.contains("getAuthType?null"));
    }

    @Test
    public void testAuthenticatedRequest() throws IOException, SAXException {
        HtmlPage page = webClient.getPage(base + "/LoginServlet?user=u1&password=p1");
        String responseText = page.asText();
        
        System.out.println("testAuthenticatedRequest:\n" + responseText + "\n");

        assertTrue(responseText.contains("isUserInRole?true"));
        assertTrue(responseText.contains("getRemoteUser?u1"));
        assertTrue(responseText.contains("getUserPrincipal?u1"));
    }
}
