package org.javaee7.jaxrs.security.declarative;

import static com.gargoylesoftware.htmlunit.HttpMethod.POST;
import static com.gargoylesoftware.htmlunit.HttpMethod.PUT;
import static com.gargoylesoftware.htmlunit.util.UrlUtils.toUrlUnsafe;
import static org.javaee7.ServerOperations.addUsersToContainerIdentityStore;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;

/**
 * @author Arun Gupta
 */
@RunWith(Arquillian.class)
public class MyResourceTest {

    @ArquillianResource
    private URL base;

    private static final String WEBAPP_SRC = "src/main/webapp";
    
    private WebClient webClient;
    private DefaultCredentialsProvider correctCreds = new DefaultCredentialsProvider();
    private DefaultCredentialsProvider incorrectCreds = new DefaultCredentialsProvider();
    
    @Before
    public void setup() {
        webClient = new WebClient();
        correctCreds.addCredentials("u1", "p1");
        incorrectCreds.addCredentials("random", "random");
    }
    
    @After
    public void tearDown() {
        webClient.close();
    }

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        
        addUsersToContainerIdentityStore();
        
        return ShrinkWrap.create(WebArchive.class)
                         .addAsWebInfResource((new File(WEBAPP_SRC + "/WEB-INF", "web.xml")))
                         .addClasses(MyApplication.class, MyResource.class);
    }

    @Test
    public void testGetWithCorrectCredentials() throws IOException, SAXException {
        webClient.setCredentialsProvider(correctCreds);
        TextPage page = webClient.getPage(base + "webresources/myresource");
        
        assertTrue(page.getContent() .contains("get"));
    }

    @Test
    public void testGetSubResourceWithCorrectCredentials() throws IOException, SAXException {
        webClient.setCredentialsProvider(correctCreds);
        TextPage page = webClient.getPage(base + "webresources/myresource/1");
        
        assertTrue(page.getContent() .contains("get1"));
    }

    @Test
    public void testGetWithIncorrectCredentials() throws IOException, SAXException {
        webClient.setCredentialsProvider(incorrectCreds);
        
        try {
            webClient.getPage(base + "webresources/myresource");
        } catch (FailingHttpStatusCodeException e) {
            assertEquals(401, e.getStatusCode());
            return;
        }
        
        fail("GET can be called with incorrect credentials");
    }

    @Test
    public void testPost() throws IOException, SAXException {
        webClient.setCredentialsProvider(correctCreds);
        
        try {
            WebRequest postRequest = new WebRequest(toUrlUnsafe(base + "webresources/myresource"), POST);
            postRequest.setRequestBody("name=myname");
            webClient.getPage(postRequest);
        } catch (FailingHttpStatusCodeException e) {
            assertEquals(403, e.getStatusCode());
            return;
        }
        
        // All methods are excluded except for GET 
        fail("POST is not authorized and can still be called");
    }

    @Test
    public void testPut() throws IOException, SAXException {
        webClient.setCredentialsProvider(correctCreds);
        
        try {
            WebRequest postRequest = new WebRequest(toUrlUnsafe(base + "webresources/myresource"), PUT);
            postRequest.setRequestBody("name=myname");
            webClient.getPage(postRequest);
        } catch (FailingHttpStatusCodeException e) {
            assertEquals(403, e.getStatusCode());
            return;
        }
        
        // All methods are excluded except for GET 
        fail("PUT is not authorized and can still be called");
    }
}
