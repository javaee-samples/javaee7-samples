package org.javaee7.jaspic.wrapping;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.javaee7.jaspic.common.ArquillianBase;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xml.sax.SAXException;

/**
 * This tests that the wrapped request and response a SAM puts into the MessageInfo structure reaches the Servlet that's
 * invoked.
 * 
 * @author Arjan Tijms
 * 
 */
@RunWith(Arquillian.class)
public class WrappingTest extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return defaultArchive();
    }

    @Test
    public void testRequestWrapping() throws IOException, SAXException {

        String response = getFromServerPath("protected/servlet");

        // The SAM wrapped a request so that it always contains the request attribute "isWrapped" with value true.
        assertTrue("Request wrapped by SAM did not arrive in Servlet.",
            response.contains("request isWrapped: true"));
    }

    @Test
    public void testResponseWrapping() throws IOException, SAXException {

        String response = getFromServerPath("protected/servlet");

        // The SAM wrapped a response so that it always contains the header "isWrapped" with value true.
        assertTrue("Response wrapped by SAM did not arrive in Servlet.",
            response.contains("response isWrapped: true"));
    }

}