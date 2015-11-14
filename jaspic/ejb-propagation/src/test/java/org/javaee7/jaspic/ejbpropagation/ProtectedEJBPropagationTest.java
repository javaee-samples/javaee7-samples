package org.javaee7.jaspic.ejbpropagation;

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
 * This tests that the established authenticated identity propagates correctly from the web layer to a "protected" EJB (an EJB
 * with declarative role checking).
 * 
 * @author Arjan Tijms
 * 
 */
@RunWith(Arquillian.class)
public class ProtectedEJBPropagationTest extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return defaultArchive();
    }

    @Test
    public void protectedServletCallingProtectedEJB() throws IOException, SAXException {

        String response = getFromServerPath("protected/servlet-protected-ejb?doLogin=true");

        // Both the web (HttpServletRequest) and EJB (EJBContext) should see the same
        // user name.
        assertTrue(
            "User should have been authenticated in the web layer and given name \"test\", " + 
            " but does not appear to have this name",
            response.contains("web username: test")
        );
        assertTrue(
            "Web has user principal set, but EJB not.", 
            response.contains("EJB username: test")
        );

        // Both the web (HttpServletRequest) and EJB (EJBContext) should see that the
        // user has the role "architect".
        assertTrue(response.contains("web user has role \"architect\": true"));
        assertTrue("Web user principal has role \"architect\", but one in EJB doesn't.",
            response.contains("EJB user has role \"architect\": true"));
    }

    /**
     * A small variation on the testProtectedServletWithLoginCallingEJB that tests if for authentication that happened for
     * public resources the security context also propagates to EJB.
     * 
     */
    @Test
    public void publicServletCallingProtectedEJB() throws IOException, SAXException {

        String response = getFromServerPath("public/servlet-protected-ejb?doLogin=true");

        // Both the web (HttpServletRequest) and EJB (EJBContext) should see the same
        // user name.
        assertTrue(
            "User should have been authenticated in the web layer and given name \"test\", " + 
            " but does not appear to have this name",
            response.contains("web username: test")
        );
        assertTrue(
            "Web has user principal set, but EJB not.", 
            response.contains("EJB username: test")
        );

        // Both the web (HttpServletRequest) and EJB (EJBContext) should see that the
        // user has the role "architect".
        assertTrue(response.contains("web user has role \"architect\": true"));
        assertTrue("Web user principal has role \"architect\", but one in EJB doesn't.",
            response.contains("EJB user has role \"architect\": true"));
    }

}