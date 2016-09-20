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
 * The JSF with CDI forward test tests that a SAM is able to forward to a JSF view
 * that uses a CDI backing bean.
 * 
 * @author Arjan Tijms
 * 
 */
@RunWith(Arquillian.class)
public class JSFCDIForwardTest extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return tryWrapEAR(
            defaultWebArchive()
                .addAsWebInfResource(resource("beans.xml"))
                .addAsWebInfResource(resource("faces-config.xml"))
                .addAsWebResource(web("forward-cdi.xhtml"))
        );
    }

    @Test
    public void testJSFwithCDIForwardViaPublicResource() throws IOException, SAXException {

        String response = getFromServerPath("public/servlet?tech=jsfcdi");
        assertTrue(
            "Response did not contain output from JSF view with CDI that SAM forwarded to.", 
            response.contains("response from JSF forward - Called from CDI")
        );
    }
    
    @Test
    public void testJSFwithCDIForwardViaProtectedResource() throws IOException, SAXException {

        String response = getFromServerPath("protected/servlet?tech=jsfcdi");
        assertTrue(
            "Response did not contain output from JSF view with CDI that SAM forwarded to.",
            response.contains("response from JSF forward - Called from CDI")
        );
    }

}