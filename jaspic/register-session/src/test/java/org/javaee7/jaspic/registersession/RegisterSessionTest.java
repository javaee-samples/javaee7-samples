package org.javaee7.jaspic.registersession;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;

import org.javaee7.jaspic.common.ArquillianBase;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;

@RunWith(Arquillian.class)
public class RegisterSessionTest extends ArquillianBase {

    @ArquillianResource
    private URL base;
    private WebClient webClient;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return defaultArchive();
    }

    @Before
    public void setUp() {
        webClient = new WebClient();
    }

    @After
    public void tearDown() {
        webClient.closeAllWindows();
    }

    @Test
    public void testRemembersSession() throws IOException, SAXException {
        

        // -------------------- Request 1 ---------------------------
        
        // Accessing protected page without login
        TextPage page = webClient.getPage(base + "protected/servlet");
        
        // Not logged-in thus should not be accessible.
        assertFalse(page.getContent().contains("This is a protected servlet"));
        

        // -------------------- Request 2 ---------------------------

        // We access the protected page again and now login
        //
        
        page = webClient.getPage(base + "protected/servlet?doLogin");

        // Now has to be logged-in so page is accessible
        assertTrue("Could not access protected page, but should be able to. "
                + "Did the container remember the previously set 'unauthenticated identity'?",
                page.getContent().contains("This is a protected servlet"));


        // -------------------- Request 3 ---------------------------

        // JASPIC is normally stateless, but for this test the SAM uses the register session feature so now
        // we should be logged-in when doing a call without explicitly logging in again.
        
        page = webClient.getPage(base + "protected/servlet?continueSession");
        
        // Logged-in thus should be accessible.
        assertTrue("Could not access protected page, but should be able to. "
                + "Did the container not remember the authenticated identity via 'javax.servlet.http.registerSession'?",
                page.getContent().contains("This is a protected servlet"));

        // Both the user name and roles/groups have to be restored

        // *** NOTE ***: The JASPIC 1.1 spec is NOT clear about remembering roles, but spec lead Ron Monzillo clarified that
        // this should indeed be the case. The next JASPIC revision of the spec will have to mention this explicitly.
        // Intuitively it should make sense though that the authenticated identity is fully restored and not partially,
        // but again the spec should make this clear to avoid ambiguity.
        assertTrue(page.getContent().contains("web username: test"));
        assertTrue(page.getContent().contains("web user has role \"architect\": true"));
        
        
        // -------------------- Request 4 ---------------------------
        
        // The session should also be remembered for other resources, including public ones
        
        page = webClient.getPage(base + "public/servlet?continueSession");
        
        // This test almost can't fail, but include for clarity
        assertTrue(page.getContent().contains("This is a public servlet"));

        // When accessing the public page, the username and roles should be restored and be available
        // just as on protected pages
        assertTrue(page.getContent().contains("web username: test"));
        assertTrue(page.getContent().contains("web user has role \"architect\": true"));
    }
    
    @Test
    public void testJoinSessionIsOptional() throws IOException, SAXException {


        // -------------------- Request 1 ---------------------------

        // We access a protected page and login
        //
        
        TextPage page = webClient.getPage(base + "protected/servlet?doLogin");

        // Now has to be logged-in so page is accessible
        assertTrue("Could not access protected page, but should be able to. "
                + "Did the container remember the previously set 'unauthenticated identity'?",
                page.getContent().contains("This is a protected servlet"));


        // -------------------- Request 2 ---------------------------

        // JASPIC is normally stateless, but for this test the SAM uses the register session feature so now
        // we should be logged-in when doing a call without explicitly logging in again.
        
        page = webClient.getPage(base + "protected/servlet?continueSession");
        
        // Logged-in thus should be accessible.
        assertTrue("Could not access protected page, but should be able to. "
                + "Did the container not remember the authenticated identity via 'javax.servlet.http.registerSession'?",
                page.getContent().contains("This is a protected servlet"));

        // Both the user name and roles/groups have to be restored

        // *** NOTE ***: The JASPIC 1.1 spec is NOT clear about remembering roles, but spec lead Ron Monzillo clarified that
        // this should indeed be the case. The next JASPIC revision of the spec will have to mention this explicitly.
        // Intuitively it should make sense though that the authenticated identity is fully restored and not partially,
        // but again the spec should make this clear to avoid ambiguity.
        assertTrue(page.getContent().contains("web username: test"));
        assertTrue(page.getContent().contains("web user has role \"architect\": true"));
        
        
        // -------------------- Request 3 ---------------------------
        
        // Although the container remembers the authentication session, the SAM needs to OPT-IN to it.
        // If the SAM instead "does nothing", we should not have access to the protected resource anymore
        // even within the same HTTP session.
        
        page = webClient.getPage(base + "protected/servlet");
        assertFalse(page.getContent().contains("This is a protected servlet"));
        
        
        // -------------------- Request 4 ---------------------------
        
        // Access to a public page is unaffected by joining or not joining the session, but if we do not join the
        // session we shouldn't see the user's name and roles.
        
        // THIS NOW FAILS ON GLASSFISH 4.0. DOUBLE CHECK WITH RON MONZILLO THAT THIS IS INDEED AN ERROR.
        
        page = webClient.getPage(base + "public/servlet");
        
        assertTrue(page.getContent().contains("This is a public servlet"));
        assertFalse(page.getContent().contains("web username: test"));
        assertFalse(page.getContent().contains("web user has role \"architect\": true"));

        
    }
}