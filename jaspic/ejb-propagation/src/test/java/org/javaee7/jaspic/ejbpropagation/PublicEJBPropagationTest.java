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
 * This tests that the established authenticated identity propagates correctly from the web layer to a "public" EJB (an EJB
 * without declarative role checking).
 * 
 * @author Arjan Tijms
 * 
 */
@RunWith(Arquillian.class)
public class PublicEJBPropagationTest extends ArquillianBase {

    @ArquillianResource
    private URL base;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return defaultArchive();
    }

    @Test
    public void testProtectedServletWithLoginCallingEJB() throws IOException, SAXException {

        WebResponse getResponse = new WebConversation().getResponse(new GetMethodWebRequest(base
                + "protected/servlet-public-ejb?doLogin"));

        // Both the web (HttpServletRequest) and EJB (EJBContext) should see the same
        // user name.
        assertTrue(getResponse.getText().contains("web username: test"));
        assertTrue("Web has user principal set, but EJB not.", getResponse.getText().contains("EJB username: test"));
    }

}