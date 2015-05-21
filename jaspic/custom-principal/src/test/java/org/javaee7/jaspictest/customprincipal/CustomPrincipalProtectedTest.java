package org.javaee7.jaspictest.customprincipal;

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
 * This tests that we can login from a protected resource (a resource for which security constraints have been set), then
 * access it and that for this type of page the custom principal correctly arrives in a Servlet.
 * 
 * @author Arjan Tijms
 * 
 */
@RunWith(Arquillian.class)
public class CustomPrincipalProtectedTest extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return defaultArchive();
    }

    @Test
    public void testProtectedPageLoggedin() throws IOException, SAXException {

        String response = getFromServerPath("protected/servlet?doLogin=true");

        // Target resource should be accessible
        assertTrue(
            "Authentication seems to have failed, as the expected response from the requested resource is not correct.",
            response.contains("This is a protected servlet")
        );
        
        // Has to be logged-in with the right principal
        assertTrue(
            "Authentication but username is not the expected one 'test'",
            response.contains("web username: test")
        );
        assertTrue(
            "Authentication succeeded and username is correct, but the expected role 'architect' is not present.",
            response.contains("web user has role \"architect\": true"));
        
        assertTrue(
            "Authentication succeeded and username and roles are correct, but principal type is not the expected custom type.",
            response.contains("isCustomPrincipal: true")
        );
    }

}