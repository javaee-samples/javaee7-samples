package org.javaee7.servlet.security.deny.uncovered;

import static com.gargoylesoftware.htmlunit.HttpMethod.POST;
import static com.gargoylesoftware.htmlunit.HttpMethod.PUT;
import static org.javaee7.ServerOperations.addUsersToContainerIdentityStore;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

    @ArquillianResource
    private URL base;

    DefaultCredentialsProvider correctCreds = new DefaultCredentialsProvider();
    DefaultCredentialsProvider incorrectCreds = new DefaultCredentialsProvider();
    WebClient webClient;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        
        addUsersToContainerIdentityStore();
        
        WebArchive war = create(WebArchive.class)
            .addClass(SecureServlet.class)
            .addAsWebInfResource((new File("src/main/webapp/WEB-INF/web.xml")));

        System.out.println(war.toString(true));
        
        return war;
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
    public void testGetMethod() throws Exception {
        webClient.setCredentialsProvider(correctCreds);
        TextPage page = webClient.getPage(base + "/SecureServlet");
        assertEquals("my GET", page.getContent());
    }

    @Test
    public void testPostMethod() throws Exception {
        webClient.setCredentialsProvider(correctCreds);
        WebRequest request = new WebRequest(new URL(base + "SecureServlet"), POST);
        
        TextPage p = null;
        try {
            p = webClient.getPage(request);
            System.out.println(p.getContent());
            
            assertFalse(
                "POST method could be called even with deny-uncovered-http-methods", 
                p.getContent().contains("my POST"));
        } catch (FailingHttpStatusCodeException e) {
            assertNotNull(e);
            assertEquals(403, e.getStatusCode());
            return;
        }
        
        fail("POST correctly not called, but wrong status code: " + (p != null ? p.getWebResponse().getStatusCode() : -1));
    }

    @Test
    public void testPutMethod() throws Exception {
        webClient.setCredentialsProvider(correctCreds);
        WebRequest request = new WebRequest(new URL(base + "SecureServlet"), PUT);
        
        System.out.println("\n\n**** After request");
        
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
