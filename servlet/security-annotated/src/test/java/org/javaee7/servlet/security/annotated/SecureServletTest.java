package org.javaee7.servlet.security.annotated;

import static com.gargoylesoftware.htmlunit.HttpMethod.POST;
import static org.javaee7.ServerOperations.addUsersToContainerIdentityStore;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author Arun Gupta
 */
@RunWith(Arquillian.class)
public class SecureServletTest {
    
    private static final String WEBAPP_SRC = "src/main/webapp";

    @ArquillianResource
    private URL base;

    private DefaultCredentialsProvider correctCreds = new DefaultCredentialsProvider();
    private DefaultCredentialsProvider incorrectCreds = new DefaultCredentialsProvider();
    private WebClient webClient;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        
        addUsersToContainerIdentityStore();
        
        return create(WebArchive.class)
                .addClass(SecureServlet.class)
                .addAsWebInfResource((new File(WEBAPP_SRC + "/WEB-INF", "web.xml")));
    }

    @Before
    public void setup() {
        correctCreds.addCredentials("u1", "p1");
        incorrectCreds.addCredentials("random", "random");
        webClient = new WebClient();
    }
    
    @After
    public void tearDown() {
        webClient.getCookieManager().clearCookies();
        webClient.close();
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
        WebRequest request = new WebRequest(new URL(base + "/SecureServlet"), POST);
        HtmlPage page = webClient.getPage(request);
        
        assertEquals("Servlet Security Annotated - Basic Auth with File-base Realm", page.getTitleText());
    }

    @Test
    public void testPostWithIncorrectCredentials() throws Exception {
        webClient.setCredentialsProvider(incorrectCreds);
        WebRequest request = new WebRequest(new URL(base + "/SecureServlet"), POST);
        
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
