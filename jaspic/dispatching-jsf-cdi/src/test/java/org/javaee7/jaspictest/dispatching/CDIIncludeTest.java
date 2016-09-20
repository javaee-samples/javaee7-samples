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
public class CDIIncludeTest extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return defaultArchive();
    }

    @Test
    public void testCDIIncludeViaPublicResource() throws IOException, SAXException {

        String response = getFromServerPath("public/servlet?dispatch=include");
        
        assertTrue(
            "Response did not contain output from public Servlet with CDI that SAM included to.", 
            response.contains("response from includedServlet - Called from CDI")
        );
        
        assertTrue(
            "Response did not contain output from target Servlet after included one.", 
            response.contains("Resource invoked")
        );
        
        assertTrue(
            "Output from included Servlet with CDI and target Servlet in wrong order.",
            response.indexOf("response from includedServlet - Called from CDI") < response.indexOf("Resource invoked")
        );
    }

}