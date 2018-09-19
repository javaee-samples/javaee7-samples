/** Portions Copyright Payara Services Limited **/
package org.javaee7.servlet.security.digest;

import static com.gargoylesoftware.htmlunit.HttpMethod.POST;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URL;

import org.javaee7.ServerOperations;
import org.javaee7.servlet.security.digest.DatabaseSetup;
import org.javaee7.servlet.security.digest.SecureServlet;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
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

    WebClient webClient;
    DefaultCredentialsProvider correctCreds = new DefaultCredentialsProvider();
    DefaultCredentialsProvider incorrectCreds = new DefaultCredentialsProvider();

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        
        ServerOperations.setupContainerJDBCIDigestIdentityStore();
        
        return create(WebArchive.class)
                 .addClasses(
                     SecureServlet.class, 
                     DatabaseSetup.class) // Adds test user and credential
                 .addAsWebInfResource(
                     new File(WEBAPP_SRC + "/WEB-INF", "web.xml"))
                 .addAsLibraries(Maven.resolver()
                     .loadPomFromFile("pom.xml")
                     .resolve("commons-codec:commons-codec")
                     .withTransitivity()
                     .as(JavaArchive.class));
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
        System.out.println("\n\n\nStarting testGetWithCorrectCredentials\n\n");
        
        webClient.setCredentialsProvider(correctCreds);
        TextPage page = webClient.getPage(base + "/SecureServlet");
        
        assertEquals("my GET", page.getContent());
    }

    @Test
    public void testGetWithIncorrectCredentials() throws Exception {
        System.out.println("\n\n\nStarting testGetWithIncorrectCredentials\n\n");
        
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
        System.out.println("\n\n\nStarting testPostWithCorrectCredentials\n\n");
        
        webClient.setCredentialsProvider(correctCreds);
        WebRequest request = new WebRequest(new URL(base + "/SecureServlet"), POST);
        TextPage page = webClient.getPage(request);
        
        assertEquals("my POST", page.getContent());
    }

    @Test
    public void testPostWithIncorrectCredentials() throws Exception {
        System.out.println("\n\n\nStarting testPostWithIncorrectCredentials\n\n");
        
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
