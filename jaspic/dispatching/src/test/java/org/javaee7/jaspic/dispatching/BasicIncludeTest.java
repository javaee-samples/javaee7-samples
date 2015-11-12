package org.javaee7.jaspic.dispatching;

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
 * The basic include test tests that a SAM is able to include a simple Servlet.
 * 
 * @author Arjan Tijms
 * 
 */
@RunWith(Arquillian.class)
public class BasicIncludeTest extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return defaultArchive();
    }

    @Test
    public void testBasicIncludeViaPublicResource() throws IOException, SAXException {

        String response = getFromServerPath("public/servlet?dispatch=include");
        
        assertTrue(
            "Response did not contain output from public Servlet that SAM included to.", 
            response.contains("response from includedServlet")
        );
        
        assertTrue(
            "Response did not contain output from target Servlet after included one.", 
            response.contains("Resource invoked")
        );
        
        assertTrue(
            "Output from included Servler and target Servlet in wrong order.",
            response.indexOf("response from includedServlet") < response.indexOf("Resource invoked")
        );
    }

}