package org.javaee7.jaspic.ejbpropagation;

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
 * This tests that the established authenticated identity propagates correctly from the web layer to a "protected" EJB (an EJB
 * with declarative role checking).
 * 
 * @author Arjan Tijms
 * 
 */
@RunWith(Arquillian.class)
public class ProtectedEJBPropagationTest extends ArquillianBase {

    @ArquillianResource
    private URL base;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return defaultArchive();
    }

    @Test
    public void testProtectedServletWithLoginCallingEJB() throws IOException, SAXException {

        WebResponse getResponse = new WebConversation().getResponse(new GetMethodWebRequest(base
                + "protected/servlet-protected-ejb?doLogin"));

        // Both the web (HttpServletRequest) and EJB (EJBContext) should see the same
        // user name.
        assertTrue(getResponse.getText().contains("web username: test"));
        assertTrue("Web has user principal set, but EJB not.", getResponse.getText().contains("EJB username: test"));

        // Both the web (HttpServletRequest) and EJB (EJBContext) should see that the
        // user has the role "architect".
        assertTrue(getResponse.getText().contains("web user has role \"architect\": true"));
        assertTrue("Web user principal has role \"architect\", but one in EJB doesn't.",
                getResponse.getText().contains("EJB user has role \"architect\": true"));
    }

    /**
     * A small variation on the testProtectedServletWithLoginCallingEJB that tests if for authentication that happened for
     * public resources the security context also propagates to EJB.
     * 
     */
    // @Test
    public void testPublicServletWithLoginCallingEJB() throws IOException, SAXException {

        WebResponse getResponse = new WebConversation().getResponse(new GetMethodWebRequest(base
                + "public/servlet-protected-ejb?doLogin"));

        // Both the web (HttpServletRequest) and EJB (EJBContext) should see the same
        // user name.
        assertTrue(getResponse.getText().contains("web username: test"));
        assertTrue("Web has user principal set, but EJB not.", getResponse.getText().contains("EJB username: test"));

        // Both the web (HttpServletRequest) and EJB (EJBContext) should see that the
        // user has the role "architect".
        assertTrue(getResponse.getText().contains("web user has role \"architect\": true"));
        assertTrue("Web user principal has role \"architect\", but one in EJB doesn't.",
                getResponse.getText().contains("EJB user has role \"architect\": true"));
    }

}