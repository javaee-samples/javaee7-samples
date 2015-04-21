package org.javaee7.servlet.metadata.complete;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.gargoylesoftware.htmlunit.*;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import static org.junit.Assert.assertEquals;

/**
 * @author Roland Huss
 *
 * Plain Test without any framework dependency accessing the test servlet directly.
 */
public class SimpleServletPlainIT {

    private WebClient webClient;
    private URL base;

    @Before
    public void setup() throws MalformedURLException {
        webClient = new WebClient();
        base = new URL(System.getProperty("test.url"));
    }

    @Test
    public void testGet() throws IOException, SAXException {
        TextPage page = webClient.getPage(base + "/SimpleServlet");
        assertEquals("my GET", page.getContent());
    }

    @Test
    public void testPost() throws IOException, SAXException {
        WebRequest request = new WebRequest(new URL(base + "/SimpleServlet"), HttpMethod.POST);
        TextPage page = webClient.getPage(request);
        assertEquals("my POST", page.getContent());
    }
}
