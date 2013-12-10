package org.javaee7.jaspic.basicauthentication;

import static org.junit.Assert.assertFalse;
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
 * This tests that we can login from a protected resource (a resource for which security constraints have been set) and then
 * access it.
 * 
 * @author Arjan Tijms
 * 
 */
@RunWith(Arquillian.class)
public class BasicAuthenticationProtectedTest extends ArquillianBase {

    @ArquillianResource
    private URL base;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return defaultArchive();
    }

    @Test
    public void testProtectedPageNotLoggedin() throws IOException, SAXException {

        WebResponse getResponse = new WebConversation().getResponse(new GetMethodWebRequest(base + "protected/servlet"));

        // Not logged-in thus should not be accessible.
        assertFalse(getResponse.getText().contains("This is a protected servlet"));
    }

    @Test
    public void testProtectedPageLoggedin() throws IOException, SAXException {

        WebResponse getResponse = new WebConversation().getResponse(new GetMethodWebRequest(base
                + "protected/servlet?doLogin=true"));

        // Now has to be logged-in so page is accessible
        assertTrue(getResponse.getText().contains("This is a protected servlet"));
    }

}