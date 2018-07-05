package org.javaee7.servlet.security.basicauth.omission;

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
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;

/**
 * @author Arun Gupta
 */
@RunWith(Arquillian.class)
public class SecureServletTest {

    private static final String WEBAPP_SRC = "src/main/webapp";

    @ArquillianResource
    private URL base;

    private WebClient webClient;
    private DefaultCredentialsProvider correctCreds = new DefaultCredentialsProvider();
    private DefaultCredentialsProvider incorrectCreds = new DefaultCredentialsProvider();

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        
        addUsersToContainerIdentityStore();
        
        return create(WebArchive.class)
                .addClass(SecureServlet.class)
                .addAsWebInfResource((new File(WEBAPP_SRC + "/WEB-INF", "web.xml")));
    }

    @Before
    public void setup() {
        webClient = new WebClient();
        correctCreds.addCredentials("u1", "p1");
        incorrectCreds.addCredentials("random", "random");
    }
    
    @After
    public void tearDown() {
        webClient.getCookieManager().clearCookies();
        webClient.close();
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
    public void testPostWithNoCredentials() throws Exception {
        WebRequest request = new WebRequest(new URL(base + "SecureServlet"), POST);
        TextPage page = webClient.getPage(request);
        
        assertEquals("my POST", page.getContent());
    }

    @Test
    public void testPostWithCorrectCredentials() throws Exception {
        webClient.setCredentialsProvider(correctCreds);
        WebRequest request = new WebRequest(new URL(base + "SecureServlet"), POST);
        TextPage page = webClient.getPage(request);
        
        assertEquals("my POST", page.getContent());
    }

    @Test
    public void testPostWithIncorrectCredentials() throws Exception {
        webClient.setCredentialsProvider(incorrectCreds);
        WebRequest request = new WebRequest(new URL(base + "SecureServlet"), POST);
        TextPage page = webClient.getPage(request);
        
        assertEquals("my POST", page.getContent());
    }

}
