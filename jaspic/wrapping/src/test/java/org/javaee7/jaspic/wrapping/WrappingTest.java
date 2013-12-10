package org.javaee7.jaspic.wrapping;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;

import org.javaee7.jaspic.common.ArquillianBase;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xml.sax.SAXException;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;

/**
 * This tests that the wrapped request and response a SAM puts into the MessageInfo structure reaches the Servlet that's
 * invoked.
 * 
 * @author Arjan Tijms
 * 
 */
@RunWith(Arquillian.class)
public class WrappingTest extends ArquillianBase {

    @ArquillianResource
    private URL base;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return defaultArchive();
    }

    @Test
    public void testRequestWrapping() throws IOException, SAXException {

        WebResponse getResponse = new WebConversation().getResponse(new GetMethodWebRequest(base + "protected/servlet"));

        // The SAM wrapped a request so that it always contains the request attribute "isWrapped" with value true.
        assertTrue("Request wrapped by SAM did not arrive in Servlet.",
                getResponse.getText().contains("request isWrapped: true"));
    }

    @Test
    public void testResponseWrapping() throws IOException, SAXException {
        WebResponse getResponse = new WebConversation().getResponse(new GetMethodWebRequest(base + "protected/servlet"));

        // The SAM wrapped a response so that it always contains the header "isWrapped" with value true.
        assertTrue("Response wrapped by SAM did not arrive in Servlet.",
                getResponse.getText().contains("response isWrapped: true"));
    }

}