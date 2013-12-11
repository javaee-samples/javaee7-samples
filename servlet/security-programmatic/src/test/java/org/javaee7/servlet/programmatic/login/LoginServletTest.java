package org.javaee7.servlet.programmatic.login;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
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
        WebConversation conv = new WebConversation();
        GetMethodWebRequest getRequest = new GetMethodWebRequest(base + "/LoginServlet");
        String responseText = conv.getResponse(getRequest).getText();

        assert(responseText.contains("isUserInRole?false"));
        assert(responseText.contains("getRemoteUser?null"));
        assert(responseText.contains("getUserPrincipal?null"));
        assert(responseText.contains("getAuthType?null"));
    }


    @Test
    public void testAuthenticatedRequest() throws IOException, SAXException {
        WebConversation conv = new WebConversation();
        GetMethodWebRequest getRequest = new GetMethodWebRequest(base + "/LoginServlet?user=u1&password=p1");
        String responseText = conv.getResponse(getRequest).getText();
        System.out.println(responseText);
        

        assert(responseText.contains("isUserInRole?true"));
        assert(responseText.contains("getRemoteUser?u1"));
        assert(responseText.contains("getUserPrincipal?u1"));
        assert(responseText.contains("getAuthType?BASIC"));
    }
}
