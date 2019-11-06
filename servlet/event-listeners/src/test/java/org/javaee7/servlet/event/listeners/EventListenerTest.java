package org.javaee7.servlet.event.listeners;

import static org.junit.Assert.assertTrue;

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

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author Arjan Tijms
 */
@RunWith(Arquillian.class)
public class EventListenerTest {

    @ArquillianResource
    private URL base;

    private WebClient webClient;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap
             .create(WebArchive.class)
             .addClasses(
                 MyServletRequestAttributeListener.class,
                 MyHttpSessionActivationListener.class,
                 MyHttpSessionAttributeListener.class,
                 MyHttpSessionBindingListener.class,
                 MyContextAttributeListener.class,
                 MyServletRequestListener.class,
                 MySessionIdListener.class,
                 MyContextListener.class,
                 MySessionListener.class,
                 TestServlet.class);
    }

    @Before
    public void setup() {
        webClient = new WebClient();
    }

    @Test
    public void testContextAttributeListener() throws IOException {
        HtmlPage page = webClient.getPage(base + "TestServlet");
        
        System.out.println(page.asText());
        
        assertTrue(page.asText().contains("MyContextAttributeListener.attributeAdded: attribute1"));
        assertTrue(page.asText().contains("MyContextAttributeListener.attributeReplaced: attribute1"));
        assertTrue(page.asText().contains("MyContextAttributeListener.attributeRemoved: attribute1"));
    }
    
    @Test
    public void testSessionListener() throws IOException {
        HtmlPage page = webClient.getPage(base + "TestServlet");
        
        assertTrue(page.asText().contains("MySessionListener.sessionCreated:"));
        assertTrue(page.asText().contains("MySessionListener.sessionDestroyed:"));
    }
    
    @Test
    public void testSessionAttributeListener() throws IOException {
        HtmlPage page = webClient.getPage(base + "TestServlet");
        
        assertTrue(page.asText().contains("MyHttpSessionAttributeListener.attributeAdded: attribute1"));
        assertTrue(page.asText().contains("MyHttpSessionAttributeListener.attributeReplaced: attribute1"));
        assertTrue(page.asText().contains("MyHttpSessionAttributeListener.attributeRemoved: attribute1"));
    }
    
    @Test
    public void testRequestAttributeListener() throws IOException {
        HtmlPage page = webClient.getPage(base + "TestServlet");
        
        assertTrue(page.asText().contains("MyServletRequestAttributeListener.attributeAdded: attribute1"));
        assertTrue(page.asText().contains("MyServletRequestAttributeListener.attributeReplaced: attribute1"));
        assertTrue(page.asText().contains("MyServletRequestAttributeListener.attributeRemoved: attribute1"));
    }
   
}
