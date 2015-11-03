package org.javaee7.servlet.programmatic.login;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xml.sax.SAXException;

/**
 * @author Arun Gupta
 */
@RunWith(Arquillian.class)
public class LoginServletTest {

    private static final String WEBAPP_SRC = "src/main/webapp";

    @ArquillianResource
    private URL base;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class).
            addClass(LoginServlet.class).
            addAsWebInfResource((new File(WEBAPP_SRC + "/WEB-INF", "web.xml")));
        return war;
    }

    @Test
    public void testUnauthenticatedRequest() throws IOException, SAXException {
        WebClient webClient = new WebClient();
        HtmlPage page = webClient.getPage(base + "/LoginServlet");
        String responseText = page.asText();

        //        WebRequest request = new WebRequest(new URL(base + "/LoginServlet"), HttpMethod.GET);
        //        WebResponse response = webClient.getWebConnection().getResponse(request);
        //        String responseText = response.getContentAsString();

        assert (responseText.contains("isUserInRole?false"));
        assert (responseText.contains("getRemoteUser?null"));
        assert (responseText.contains("getUserPrincipal?null"));
        assert (responseText.contains("getAuthType?null"));
    }

    @Test
    public void testAuthenticatedRequest() throws IOException, SAXException {
        WebClient webClient = new WebClient();
        WebRequest request = new WebRequest(new URL(base + "/LoginServlet?user=u1&password=p1"), HttpMethod.GET);
        WebResponse response = webClient.getWebConnection().getResponse(request);
        String responseText = response.getContentAsString();
        System.out.println(responseText);

        assert (responseText.contains("isUserInRole?true"));
        assert (responseText.contains("getRemoteUser?u1"));
        assert (responseText.contains("getUserPrincipal?u1"));
        assert (responseText.contains("getAuthType?BASIC"));
    }
}
