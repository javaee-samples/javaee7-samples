package org.javaee7.servlet.error.mapping;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * @author Arjan Tijms
 */
@RunWith(Arquillian.class)
public class ErrorMappingTest {
    
    private static final String WEBAPP_SRC = "src/main/webapp";

    @ArquillianResource
    private URL base;

    private WebClient webClient;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                         .addAsWebInfResource((new File(WEBAPP_SRC + "/WEB-INF", "web.xml")))
                         .addClasses(
                             TestServlet.class, 
                             ErrorServlet.class,
                             NotFoundServlet.class);
    }

    @Before
    public void setup() {
        webClient = new WebClient();
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
    }

    @Test
    public void testError() throws IOException, SAXException {
        TextPage page = webClient.getPage(base + "TestServlet");
        
        System.out.println(page.getContent());
        
        assertTrue(page.getContent().contains("!error!"));
    }
    
    @Test
    public void test404() throws IOException, SAXException {
        TextPage page = webClient.getPage(base + "does-not-exist");
        
        assertTrue(page.getContent().contains("!not found!"));
    }
   
}
