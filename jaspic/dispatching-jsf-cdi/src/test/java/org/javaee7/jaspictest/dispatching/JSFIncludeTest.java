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
 * The JSF with CDI forward test tests that a SAM is able to include a plain JSF view.
 * 
 * @author Arjan Tijms
 * 
 */
@RunWith(Arquillian.class)
public class JSFIncludeTest extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return tryWrapEAR(
                defaultWebArchive()
                    .addAsWebInfResource(resource("faces-config.xml"))
                    .addAsWebResource(web("include.xhtml"))
            );
    }

    @Test
    public void testJSFIncludeViaPublicResource() throws IOException, SAXException {

        String response = getFromServerPath("public/servlet?dispatch=include&tech=jsf");
        
        assertTrue(
            "Response did not contain output from JSF view that SAM included.",
            response.contains("response from JSF include")
        );
        
        assertTrue(
            "Response did not contain output from target Servlet after included JSF view.", 
            response.contains("Resource invoked")
        );
        
        assertTrue(
            "Output from included JSF view and target Servlet in wrong order.",
            response.indexOf("response from JSF include") < response.indexOf("Resource invoked")
        );
    }

}