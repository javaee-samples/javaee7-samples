package org.javaee7.jaspic.programmaticauthentication;

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
 * This tests that a call from a Servlet to HttpServletRequest#authenticate can result
 * in a successful authentication.
 * 
 * @author Arjan Tijms
 * 
 */
@RunWith(Arquillian.class)
public class ProgrammaticAuthenticationTest extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return defaultArchive();
    }

    @Test
    public void testAuthenticate() throws IOException, SAXException {
        assertAuthenticated(getFromServerPath("public/authenticate"));
    }
    
    @Test
    public void testAuthenticateFailFirstOnce() throws IOException, SAXException {
        // Before authenticating successfully, call request.authenticate which
        // is known to fail (do nothing)
        assertAuthenticated(getFromServerPath("public/authenticate?failFirst=1"));
    }
    
    @Test
    public void testAuthenticateFailFirstTwice() throws IOException, SAXException {
        // Before authenticating successfully, call request.authenticate twice which
        // are both known to fail (do nothing)
        assertAuthenticated(getFromServerPath("public/authenticate?failFirst=2"));
    }
    
    private void assertAuthenticated(String response) {
        
        // Should not be authenticated in the "before" case, which is 
        // before request.authentiate is called
        assertTrue(
            "Should not be authenticated yet, but a username other than null was encountered. " +
            "This is not correct.",
            response.contains("before web username: null")
        );
        assertTrue(
            "Should not be authenticated yet, but the user seems to have the role \"architect\". " +
            "This is not correct.",
            response.contains("before web user has role \"architect\": false")
        );
        
        // The main request.authenticate causes the SAM to be called which always authenticates
        assertTrue(
                "Calling request.authenticate should have returned true, but did not.",
                response.contains("request.authenticate outcome: true")
            );
        
        // Should be authenticated in the "after" case, which is 
        // after request.authentiate is called
        assertTrue(
            "User should have been authenticated and given name \"test\", " + 
            " but does not appear to have this name",
            response.contains("after web username: test")
        );
        assertTrue(
            "User should have been authenticated and given role \"architect\", " + 
            " but does not appear to have this role",
            response.contains("after web user has role \"architect\": true")
        );
    }
   

}