package org.javaee7.jsf.http.get;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
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

/**
 * @author Arun Gupta
 */
@RunWith(Arquillian.class)
public class UserTest {

    @ArquillianResource
    private URL base;

    WebClient webClient;

    private static final String WEBAPP_SRC = "src/main/webapp";
    HtmlPage page;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class).
            addClass(User.class)
            .addAsWebResource(new File(WEBAPP_SRC, "index.xhtml"))
            .addAsWebResource(new File(WEBAPP_SRC, "index2.xhtml"))
            .addAsWebResource(new File(WEBAPP_SRC, "login.xhtml"))
            .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF", "web.xml"))
            .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF", "beans.xml"));
    }

    @Before
    public void setup() throws IOException {
        webClient = new WebClient();
        page = webClient.getPage(base + "/faces/index.xhtml");
    }

    @Test
    public void testLink() throws IOException {
        HtmlAnchor anchor = (HtmlAnchor) page.getElementById("link1");
        assertTrue(anchor.getHrefAttribute().contains("faces/login.xhtml"));
        assertEquals("Login1", anchor.asText());

        HtmlPage output = anchor.click();
        assertEquals("HTTP GET (Login)", output.getTitleText());
    }

    @Test
    public void testLinkWithParam() throws IOException {
        HtmlAnchor anchor = (HtmlAnchor) page.getElementById("link2");
        assertTrue(anchor.getHrefAttribute().contains("faces/login.xhtml"));
        assertTrue(anchor.getHrefAttribute().contains("?name=Jack"));
        assertEquals("Login2", anchor.asText());

        HtmlPage output = anchor.click();
        assertEquals("HTTP GET (Login)", output.getTitleText());
    }

    @Test
    public void testLinkWithPreProcessParams() {
        HtmlAnchor anchor = (HtmlAnchor) page.getElementById("link3");
        assertEquals("Login3", anchor.asText());
        assertTrue(anchor.getHrefAttribute().contains("faces/index2.xhtml"));
        assertTrue(anchor.getHrefAttribute().contains("?name=Jack"));
    }

    @Test
    public void testButton() throws IOException {
        HtmlButtonInput button = (HtmlButtonInput) page.getElementById("button1");
        assertEquals("Login4", button.asText());

        HtmlPage output = button.click();
        assertEquals("HTTP GET (Login)", output.getTitleText());
    }
}
