package org.javaee7.servlet.metadata.complete;

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
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xml.sax.SAXException;

/**
 * @author Arun Gupta
 */
@RunWith(Arquillian.class)
public class TestServletTest {

    private static final String WEBAPP_SRC = "src/main/webapp";

    @ArquillianResource
    private URL base;

    WebClient webClient;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class).
            addClass(TestServlet.class).
            addAsWebInfResource((new File(WEBAPP_SRC + "/WEB-INF", "web.xml")));
        return war;
    }

    @Before
    public void setup() {
        webClient = new WebClient();
    }

    @Test
    public void testGet() throws IOException, SAXException {
        TextPage page = webClient.getPage(base + "TestServlet");
        assertEquals("my GET", page.getContent());
    }

    @Test
    public void testPost() throws IOException, SAXException {
        WebRequest request = new WebRequest(new URL(base + "TestServlet"), HttpMethod.POST);
        TextPage page = webClient.getPage(request);
        assertEquals("my POST", page.getContent());
    }
}
