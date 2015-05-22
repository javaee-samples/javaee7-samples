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
 * The JSF with CDI forward test tests that a SAM is able to include a JSF view
 * that uses a CDI backing bean.
 * 
 * @author Arjan Tijms
 * 
 */
@RunWith(Arquillian.class)
public class JSFCDIIncludeTest extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return tryWrapEAR(
                defaultWebArchive()
                    .addAsWebInfResource(resource("beans.xml"))
                    .addAsWebInfResource(resource("faces-config.xml"))
                    .addAsWebResource(web("include-cdi.xhtml"))
            );
    }

    @Test
    public void testJSFwithCDIIncludeViaPublicResource() throws IOException, SAXException {

        String response = getFromServerPath("public/servlet?dispatch=include&tech=jsfcdi");
        
        assertTrue(
            "Response did not contain output from JSF view that SAM included.", 
            response.contains("response from JSF include - Called from CDI")
        );
        
        assertTrue(
            "Response did not contain output from target Servlet after included JSF view.", 
            response.contains("Resource invoked")
        );
        
        assertTrue(
            "Output from included JSF view and target Servlet in wrong order.",
            response.indexOf("response from JSF include - Called from CDI") < response.indexOf("Resource invoked")
        );
    }

}