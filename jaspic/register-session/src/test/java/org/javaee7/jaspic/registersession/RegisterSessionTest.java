package org.javaee7.jaspic.registersession;

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

@RunWith(Arquillian.class)
public class RegisterSessionTest extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return defaultArchive();
    }

    @Test
    public void testRemembersSession() throws IOException, SAXException {

        // -------------------- Request 1 ---------------------------

        // Accessing protected page without login
        String response = getFromServerPath("protected/servlet");

        // Not logged-in thus should not be accessible.
        assertFalse(response.contains("This is a protected servlet"));

        // -------------------- Request 2 ---------------------------

        // We access the protected page again and now login

        response = getFromServerPath("protected/servlet?doLogin=true");

        // Now has to be logged-in so page is accessible
        assertTrue(
            "Could not access protected page, but should be able to. " + 
            "Did the container remember the previously set 'unauthenticated identity'?",
            response.contains("This is a protected servlet")
        );

        // -------------------- Request 3 ---------------------------

        // JASPIC is normally stateless, but for this test the SAM uses the register session feature so now
        // we should be logged-in when doing a call without explicitly logging in again.

        response = getFromServerPath("protected/servlet?continueSession=true");

        // Logged-in thus should be accessible.
        assertTrue(
            "Could not access protected page, but should be able to. " + 
            "Did the container not remember the authenticated identity via 'javax.servlet.http.registerSession'?",
            response.contains("This is a protected servlet")
        );

        // Both the user name and roles/groups have to be restored

        // *** NOTE ***: The JASPIC 1.1 spec is NOT clear about remembering roles, but spec lead Ron Monzillo clarified that
        // this should indeed be the case. The next JASPIC revision of the spec will have to mention this explicitly.
        // Intuitively it should make sense though that the authenticated identity is fully restored and not partially,
        // but again the spec should make this clear to avoid ambiguity.
        assertTrue(response.contains("web username: test"));
        assertTrue(response.contains("web user has role \"architect\": true"));

        // -------------------- Request 4 ---------------------------

        // The session should also be remembered for other resources, including public ones

        response = getFromServerPath("public/servlet?continueSession=true");

        // This test almost can't fail, but include for clarity
        assertTrue(response.contains("This is a public servlet"));

        // When accessing the public page, the username and roles should be restored and be available
        // just as on protected pages
        assertTrue(response.contains("web username: test"));
        assertTrue(response.contains("web user has role \"architect\": true"));
    }

    @Test
    public void testJoinSessionIsOptional() throws IOException, SAXException {

        // -------------------- Request 1 ---------------------------

        // We access a protected page and login
        //

        String response = getFromServerPath("protected/servlet?doLogin=true");

		// Now has to be logged-in so page is accessible
		assertTrue(
			"Could not access protected page, but should be able to. " + 
	        "Did the container remember the previously set 'unauthenticated identity'?",
			response.contains("This is a protected servlet")
		);

        // -------------------- Request 2 ---------------------------

        // JASPIC is normally stateless, but for this test the SAM uses the register session feature so now
        // we should be logged-in when doing a call without explicitly logging in again.

        response = getFromServerPath("protected/servlet?continueSession=true");

        // Logged-in thus should be accessible.
        assertTrue(
            "Could not access protected page, but should be able to. " + 
            "Did the container not remember the authenticated identity via 'javax.servlet.http.registerSession'?",
            response.contains("This is a protected servlet")
        );

        // Both the user name and roles/groups have to be restored

        // *** NOTE ***: The JASPIC 1.1 spec is NOT clear about remembering roles, but spec lead Ron Monzillo clarified that
        // this should indeed be the case. The next JASPIC revision of the spec will have to mention this explicitly.
        // Intuitively it should make sense though that the authenticated identity is fully restored and not partially,
        // but again the spec should make this clear to avoid ambiguity.
        assertTrue(response.contains("web username: test"));
        assertTrue(response.contains("web user has role \"architect\": true"));

        // -------------------- Request 3 ---------------------------

        // Although the container remembers the authentication session, the SAM needs to OPT-IN to it.
        // If the SAM instead "does nothing", we should not have access to the protected resource anymore
        // even within the same HTTP session.

        response = getFromServerPath("protected/servlet");
        assertFalse(response.contains("This is a protected servlet"));

        // -------------------- Request 4 ---------------------------

        // Access to a public page is unaffected by joining or not joining the session, but if we do not join the
        // session we shouldn't see the user's name and roles.

        response = getFromServerPath("public/servlet");

        assertTrue(response.contains("This is a public servlet"));
        assertFalse(response.contains("web username: test"));
        assertFalse(response.contains("web user has role \"architect\": true"));
    }
}