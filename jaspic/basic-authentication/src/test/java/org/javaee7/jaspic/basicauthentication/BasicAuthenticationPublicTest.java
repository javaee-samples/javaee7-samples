package org.javaee7.jaspic.basicauthentication;

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
 * This tests that we can login from a public page (a page for which no security constraints have been set).
 * 
 * @author Arjan Tijms
 * 
 */
@RunWith(Arquillian.class)
public class BasicAuthenticationPublicTest extends ArquillianBase {

    @ArquillianResource
    private URL base;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return defaultArchive();
    }

    @Test
    public void testPublicPageNotLoggedin() throws IOException, SAXException {

        WebResponse response = new WebConversation().getResponse(new GetMethodWebRequest(base + "public/servlet"));

        // Not logged-in
        assertTrue(response.getText().contains("web username: null"));
        assertTrue(response.getText().contains("web user has role \"architect\": false"));
    }

    @Test
    public void testPublicPageLoggedin() throws IOException, SAXException {

        // JASPIC has to be able to authenticate a user when accessing a public (non-protected) resource.

        WebResponse response = new WebConversation().getResponse(new GetMethodWebRequest(base + "public/servlet?doLogin"));

        // Now has to be logged-in
        assertTrue(response.getText().contains("web username: test"));
        assertTrue(response.getText().contains("web user has role \"architect\": true"));
    }

    @Test
    public void testPublicPageNotRememberLogin() throws IOException, SAXException {

        WebConversation conversion = new WebConversation();

        // -------------------- Request 1 ---------------------------

        WebResponse response = conversion.getResponse(new GetMethodWebRequest(base + "public/servlet"));

        // Not logged-in
        assertTrue(response.getText().contains("web username: null"));
        assertTrue(response.getText().contains("web user has role \"architect\": false"));

        System.out.println(response.getHeaderField("cookie"));

        // -------------------- Request 2 ---------------------------

        response = new WebConversation().getResponse(new GetMethodWebRequest(base + "public/servlet?doLogin"));

        // Now has to be logged-in
        assertTrue(response.getText().contains("web username: test"));
        assertTrue(response.getText().contains("web user has role \"architect\": true"));

        // -------------------- Request 3 ---------------------------

        response = conversion.getResponse(new GetMethodWebRequest(base + "public/servlet"));

        // Not logged-in
        assertTrue(response.getText().contains("web username: null"));
        assertTrue(response.getText().contains("web user has role \"architect\": false"));
    }

}