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
 * The JSF with CDI forward test tests that a SAM is able to forward to a plain JSF view.
 * 
 * @author Arjan Tijms
 * 
 */
@RunWith(Arquillian.class)
public class JSFForwardTest extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return tryWrapEAR(
            defaultWebArchive()
                .addAsWebInfResource(resource("faces-config.xml"))
                .addAsWebResource(web("forward.xhtml"))
        );
    }

    @Test
    public void testJSFForwardViaPublicResource() throws IOException, SAXException {

        String response = getFromServerPath("public/servlet?tech=jsf");
        assertTrue(
            "Response did not contain output from JSF view that SAM forwarded to.", 
            response.contains("response from JSF forward")
        );
    }
    
    @Test
    public void testJSFForwardViaProtectedResource() throws IOException, SAXException {

        String response = getFromServerPath("protected/servlet?tech=jsf");
        assertTrue(
            "Response did not contain output from JSF view that SAM forwarded to.",
            response.contains("response from JSF forward")
        );
    }

}