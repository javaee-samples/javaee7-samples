package org.javaee7.servlet.security.allow.uncovered;

import static com.gargoylesoftware.htmlunit.HttpMethod.POST;
import static com.gargoylesoftware.htmlunit.HttpMethod.PUT;
import static org.javaee7.ServerOperations.addUsersToContainerIdentityStore;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

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
 * @author Arjan Tijms 
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
        
        TextPage page = null;
        try {
            page = webClient.getPage(request);
            System.out.println(page.getContent());
            
            assertTrue(
                "POST method could not be called even without deny-uncovered-http-methods", 
                page.getContent().contains("my POST"));
        } catch (FailingHttpStatusCodeException e) {
            assertNotEquals("Post denied, but should be allowed", 403, e.getStatusCode());
            throw e;
        }
    }

    @Test
    public void testPutMethod() throws Exception {
        webClient.setCredentialsProvider(correctCreds);
        WebRequest request = new WebRequest(new URL(base + "SecureServlet"), PUT);
        
        TextPage page = null;
        try {
            page = webClient.getPage(request);
            System.out.println(page.getContent());
        } catch (FailingHttpStatusCodeException e) {
            assertNotEquals("PUT denied, but should be allowed", 403, e.getStatusCode());
            throw e;
        }
        
    }
}
