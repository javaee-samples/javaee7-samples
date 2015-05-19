package org.javaee7.jaspictest.dispatching;

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
 * The basic forward test tests that a SAM is able to forward to a simple Servlet.
 * 
 * @author Arjan Tijms
 * 
 */
@RunWith(Arquillian.class)
public class CDIForwardTest extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return tryWrapEAR(
            defaultWebArchive()
                .addAsWebInfResource(resource("beans.xml"))
        );
    }

    @Test
    public void testCDIForwardViaPublicResource() throws IOException, SAXException {

        String response = getFromServerPath("public/servlet");
        assertTrue(
            "Response did not contain output from public Servlet with CDI that SAM forwarded to.", 
            response.contains("response from forwardedServlet - Called from CDI")
        );
    }
    
    @Test
    public void testCDIForwardViaProtectedResource() throws IOException, SAXException {

        String response = getFromServerPath("protected/servlet");
        assertTrue(
            "Response did not contain output from protected Servlet with CDI that SAM forwarded to.", 
            response.contains("response from forwardedServlet - Called from CDI")
        );
    }

}