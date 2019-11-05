package org.javaee7.servlet.cookies;

import static org.junit.Assert.assertFalse;
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
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author Arjan Tijms
 */
@RunWith(Arquillian.class)
public class SimpleServletTest {

    @ArquillianResource
    private URL base;

    private WebClient webClient;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                         .addClasses(TestServlet.class, ClientCookieServlet.class);
    }

    @Before
    public void setup() {
        webClient = new WebClient();
    }

    @Test
    public void testCookies() throws IOException, SAXException {
        HtmlPage page = webClient.getPage(base + "TestServlet");
        
        assertFalse(page.asText().contains("Found cookie: myCookieKey Found cookie: myHttpOnlyCookieKey"));
        
        // Request page again, should now send cookies back
        page = webClient.getPage(base + "TestServlet");
        
        assertTrue(page.asText().contains("Found cookie: myCookieKey Found cookie: myHttpOnlyCookieKey"));
    }
    
    @Test
    public void testHttpOnlyCookies() throws IOException, SAXException {
        HtmlPage page = webClient.getPage(base + "TestServlet");
        
        assertFalse(page.asText().contains("Found cookie: myCookieKey Found cookie: myHttpOnlyCookieKey"));
        
        // Request page with client-side script, should now be able to read cookies client-side
        page = webClient.getPage(base + "ClientCookieServlet");
        webClient.waitForBackgroundJavaScript(1000);
        
        System.out.println(page.asText());
        
        assertTrue(page.asText().contains("myCookieKey"));
        assertFalse(page.asText().contains("myHttpOnlyCookieKey"));
        
        System.out.println(page.asText());
    }
   
}
