package org.javaee7.servlet.metadata.complete;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.assertTrue;
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

    /**
     * Arquillian specific method for creating a file which can be deployed
     * while executing the test.
     *
     * @return a war file
     */
    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class).
                addClass(TestServlet.class).
                addAsWebInfResource((new File(WEBAPP_SRC + "/WEB-INF", "web.xml")));
        return war;
    }

    /**
     * Test of processRequest method, of class TestServlet.
     */
    @Test
    public void testProcessRequest() throws IOException, SAXException {
        WebConversation conv = new WebConversation();
        GetMethodWebRequest getRequest = new GetMethodWebRequest(base + "/TestServlet");
        WebResponse getResponse = conv.getResponse(getRequest);
        assertTrue(getResponse.getText().contains("<title>Servlet url-pattern in web.xml</title>"));
    }
}
