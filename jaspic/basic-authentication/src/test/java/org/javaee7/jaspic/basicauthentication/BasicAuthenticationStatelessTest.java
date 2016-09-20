package org.javaee7.jaspic.basicauthentication;

import static org.junit.Assert.assertFalse;
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
 * 
 * @author Arjan Tijms
 * 
 */
@RunWith(Arquillian.class)
public class BasicAuthenticationStatelessTest extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return defaultArchive();
    }

    /**
     * Tests that access to a protected page does not depend on the authenticated identity that was established in a previous
     * request.
     */
    @Test
    public void testProtectedAccessIsStateless() throws IOException, SAXException {

        // -------------------- Request 1 ---------------------------

        // Accessing protected page without login
        String response = getFromServerPath("protected/servlet");

        // Not logged-in thus should not be accessible.
        assertFalse(response.contains("This is a protected servlet"));

        
        // -------------------- Request 2 ---------------------------

        // JASPIC is stateless and login (re-authenticate) has to happen for every request
        //
        // If the following fails but "testProtectedPageLoggedin" has succeeded,
        // the container has probably remembered the "unauthenticated identity", e.g. it has remembered that
        // we're not authenticated and it will deny further attempts to authenticate. This may happen when
        // the container does not correctly recognize the JASPIC protocol for "do nothing".

        response = getFromServerPath("protected/servlet?doLogin=true");

        // Now has to be logged-in so page is accessible
        assertTrue(
            "Could not access protected page, but should be able to. " + 
            "Did the container remember the previously set 'unauthenticated identity'?",
            response.contains("This is a protected servlet")
        );

        
        // -------------------- Request 3 ---------------------------

        // JASPIC is stateless and login (re-authenticate) has to happen for every request
        //
        // In the following method we do a call without logging in after one where we did login.
        // The container should not remember this login and has to deny access.
        response = getFromServerPath("protected/servlet");

        // Not logged-in thus should not be accessible.
        assertFalse(
            "Could access protected page, but should not be able to. " + 
            "Did the container remember the authenticated identity that was set in previous request?",
            response.contains("This is a protected servlet")
        );
    }

    /**
     * Tests that access to a protected page does not depend on the authenticated identity that was established in a previous
     * request, but use a different request order than the previous test.
     */
    @Test
    public void testProtectedAccessIsStateless2() throws IOException, SAXException {

        // -------------------- Request 1 ---------------------------

        // Start with doing a login
        String response = getFromServerPath("protected/servlet?doLogin=true");

        
        // -------------------- Request 2 ---------------------------

        // JASPIC is stateless and login (re-authenticate) has to happen for every request
        //
        // In the following method we do a call without logging in after one where we did login.
        // The container should not remember this login and has to deny access.

        // Accessing protected page without login
        response = getFromServerPath("protected/servlet");

        // Not logged-in thus should not be accessible.
        assertFalse(
            "Could access protected page, but should not be able to. " + 
            "Did the container remember the authenticated identity that was set in the previous request?",
            response.contains("This is a protected servlet")
        );
    }
    
    /**
     * Tests that access to a public page does not depend on the authenticated identity that was established in a previous
     * request.
     */
    @Test
    public void testPublicAccessIsStateless() throws IOException, SAXException {

        // -------------------- Request 1 ---------------------------

        String response = getFromServerPath("public/servlet");

        // Establish that we're initially not logged-in
        assertTrue(
            "Not authenticated, but a username other than null was encountered. " +
            "This is not correct.",
            response.contains("web username: null")
        );
        assertTrue(
            "Not authenticated, but the user seems to have the role \"architect\". " +
            "This is not correct.",
            response.contains("web user has role \"architect\": false")
        );

        
        // -------------------- Request 2 ---------------------------

        response = getFromServerPath("public/servlet?doLogin=true");

        // Now has to be logged-in
        assertTrue(
            "User should have been authenticated and given name \"test\", " + 
            " but does not appear to have this name",
            response.contains("web username: test")
        );
        assertTrue(response.contains("web user has role \"architect\": true"));

        
        // -------------------- Request 3 ---------------------------

        // Accessing public page without login
        response = getFromServerPath("public/servlet");

        // No details should linger around
        assertTrue(
            "Should not be authenticated, but a username other than null was encountered. " +
            "Did the container remember the authenticated identity that was set in the previous request?",    
            response.contains("web username: null")
        );
        assertTrue(
            "The unauthenticated user has the role 'architect', which should not be the case. " + 
            "The container seemed to have remembered it from the previous request.",
            response.contains("web user has role \"architect\": false")
        );
    }

    /**
     * Tests independently from being able to access a protected resource if any details of a previously established
     * authenticated identity are remembered
     */
    @Test
    public void testProtectedThenPublicAccessIsStateless() throws IOException, SAXException {

        // -------------------- Request 1 ---------------------------

        // Accessing protected page with login
        String response = getFromServerPath("protected/servlet?doLogin=true");

        
        // -------------------- Request 2 ---------------------------

        // Accessing public page without login
        response = getFromServerPath("public/servlet");

        // No details should linger around
        assertFalse(
            "User principal was 'test', but it should be null here. " +
            "The container seemed to have remembered it from the previous request.",
            response.contains("web username: test")
        );
        assertTrue(
            "User principal was not null, but it should be null here. ",
            response.contains("web username: null")
        );
        assertTrue(
            "The unauthenticated user has the role 'architect', which should not be the case. " + 
            "The container seemed to have remembered it from the previous request.",
            response.contains("web user has role \"architect\": false")
        );
    }

}