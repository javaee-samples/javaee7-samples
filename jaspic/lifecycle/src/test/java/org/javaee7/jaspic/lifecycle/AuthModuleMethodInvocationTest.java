package org.javaee7.jaspic.lifecycle;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;

import javax.security.auth.message.module.ServerAuthModule;
import javax.servlet.http.HttpServletRequest;

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
 * This tests that the two main methods of a SAM, {@link ServerAuthModule#validateRequest} and
 * {@link ServerAuthModule#secureResponse} are called at the right time, which is resp. before and after the resource (e.g. a
 * Servlet) is invoked.
 * 
 * @author Arjan Tijms
 * 
 */
@RunWith(Arquillian.class)
public class AuthModuleMethodInvocationTest extends ArquillianBase {

    @ArquillianResource
    private URL base;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return defaultArchive();
    }

    /**
     * Test that the main SAM methods are called and are called in the correct order.
     * 
     * The rule seems simple:
     * <ul>
     * <li>First call validateRequest() in the SAM.
     * <li>Then invoke the requested resource (e.g. a Servlet or JSP page)
     * <li>Finally call secureResponse() in the SAM
     * </ul>
     */
    @Test
    public void testBasicSAMMethodsCalled() throws IOException, SAXException {

        WebResponse getResponse = new WebConversation().getResponse(new GetMethodWebRequest(base + "protected/servlet"));

        // First test if individual methods are called
        assertTrue("SAM method validateRequest not called, but should have been.",
                getResponse.getText().contains("validateRequest invoked"));
        assertTrue("Resource (Servlet) not invoked, but should have been.", getResponse.getText().contains("Resource invoked"));

        // The previous two methods are rare to not be called, but secureResponse is more likely to fail. Seemingly it's hard
        // to understand what this method should do exactly.
        assertTrue("SAM method secureResponse not called, but should have been.",
                getResponse.getText().contains("secureResponse invoked"));

        // Finally the order should be correct. More than a few implementations call secureResponse before the resource is
        // invoked.
        assertTrue("SAM methods called in wrong order",
                getResponse.getText().contains("validateRequest invoked\nResource invoked\nsecureResponse invoked\n"));
    }

    /**
     * Test that the SAM's cleanSubject method is called following a call to {@link HttpServletRequest#logout()}.
     * <p>
     * Although occasionally a JASPIC 1.0 implementation indeed does this, it's only mandated that this happens in JASPIC 1.1
     */
    @Test
    public void testLogout() throws IOException, SAXException {

        // Note that we don't explicitly log-in; the test SAM uses for this test does that automatically before the resource
        // (servlet)
        // is invoked. Once we reach the Servlet we should be logged-in and can proceed to logout.
        WebResponse getResponse = new WebConversation()
                .getResponse(new GetMethodWebRequest(base + "protected/servlet?doLogout"));

        assertTrue("SAM method cleanSubject not called, but should have been.",
                getResponse.getText().contains("cleanSubject invoked"));
    }

}