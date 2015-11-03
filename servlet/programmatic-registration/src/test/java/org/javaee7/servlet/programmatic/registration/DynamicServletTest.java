package org.javaee7.servlet.programmatic.registration;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertEquals;

/**
 * @author OrelGenya
 */
@RunWith(Arquillian.class)
public class DynamicServletTest {

    @ArquillianResource
    private URL base;

    WebClient webClient;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class).
            addClass(DynamicServlet.class).
            addClass(SimpleServletContextListener.class);
        return war;
    }

    @Before
    public void setup() {
        webClient = new WebClient();
    }

    @Test
    public void testChildServlet() throws IOException, SAXException {
        TextPage page = webClient.getPage(base + "dynamic");
        assertEquals("dynamic GET", page.getContent());
    }
}
