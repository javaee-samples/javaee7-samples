package org.javaee7.servlet.programmatic.registration;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
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
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.xml.sax.SAXException;

/**
 * @author arungupta
 */
@RunWith(Arquillian.class)
public class ServletTest {
    
    @ArquillianResource
    private URL base;
    
    WebClient webClient;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class).
                addClass(ParentServlet.class).
                addClass(ChildServlet.class);
        return war;
    }
    
    @Before
    public void setup() {
        webClient = new WebClient();
    }

    @Test
    public void testChildServlet() throws IOException, SAXException {
        try {
            webClient.getPage(base + "/ChildServlet");
        } catch (FailingHttpStatusCodeException e) {
            assertNotNull(e);
            assertEquals(404, e.getStatusCode());
            return;
        }
        fail("/ChildSevlet could be accessed with programmatic registration");
        webClient.getPage(base + "/ParentServlet");
        webClient.getPage(base + "/ChildServlet");
    }
}
