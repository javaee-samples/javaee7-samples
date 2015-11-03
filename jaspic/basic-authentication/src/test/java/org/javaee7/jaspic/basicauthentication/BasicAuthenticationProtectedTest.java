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
 * This tests that we can login from a protected resource (a resource for which security constraints have been set) and then
 * access it.
 * 
 * @author Arjan Tijms
 * 
 */
@RunWith(Arquillian.class)
public class BasicAuthenticationProtectedTest extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return defaultArchive();
    }

    @Test
    public void testProtectedPageNotLoggedin() throws IOException, SAXException {

        String response = getFromServerPath("protected/servlet");

        // Not logged-in thus should not be accessible.
        assertFalse(response.contains("This is a protected servlet"));
    }

    @Test
    public void testProtectedPageLoggedin() throws IOException, SAXException {

        String response = getFromServerPath("protected/servlet?doLogin=true");

        // Now has to be logged-in so page is accessible
        assertTrue(response.contains("This is a protected servlet"));
    }

}