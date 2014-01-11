package org.javaee7.jaspic.basicauthentication;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.javaee7.jaspic.common.ArquillianBase;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xml.sax.SAXException;

/**
 * This tests that we can login from a public page (a page for which no security constraints have been set).
 * 
 * @author Arjan Tijms
 * 
 */
@RunWith(Arquillian.class)
public class BasicAuthenticationPublicTest extends ArquillianBase {

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return defaultArchive();
    }

    @Test
    public void testPublicPageNotLoggedin() throws IOException, SAXException {
        
        String response = getFromServerPath("public/servlet");

        // Not logged-in
        assertTrue(response.contains("web username: null"));
        assertTrue(response.contains("web user has role \"architect\": false"));
    }

    @Test
    public void testPublicPageLoggedin() throws IOException, SAXException {

        // JASPIC has to be able to authenticate a user when accessing a public (non-protected) resource.
        
        String response = getFromServerPath("public/servlet?doLogin");

        // Now has to be logged-in
        assertTrue(response.contains("web username: test"));
        assertTrue(response.contains("web user has role \"architect\": true"));
    }

    @Test
    public void testPublicPageNotRememberLogin() throws IOException, SAXException {


        // -------------------- Request 1 ---------------------------
        
        String response = getFromServerPath("public/servlet");

        // Not logged-in
        assertTrue(response.contains("web username: null"));
        assertTrue(response.contains("web user has role \"architect\": false"));


        // -------------------- Request 2 ---------------------------

        response = getFromServerPath("public/servlet?doLogin");

        // Now has to be logged-in
        assertTrue(response.contains("web username: test"));
        assertTrue(response.contains("web user has role \"architect\": true"));

        
        // -------------------- Request 3 ---------------------------

        response = getFromServerPath("public/servlet");

        // Not logged-in
        assertTrue(response.contains("web username: null"));
        assertTrue(response.contains("web user has role \"architect\": false"));
    }

}