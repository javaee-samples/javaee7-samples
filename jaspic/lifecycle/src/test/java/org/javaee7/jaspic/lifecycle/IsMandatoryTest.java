package org.javaee7.jaspic.lifecycle;

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
 * This tests that the "javax.security.auth.message.MessagePolicy.isMandatory" key
 * in the message info map is "true" for a protected resource, and not "true" for
 * a public resource.
 * 
 * @author Arjan Tijms
 * 
 */
@RunWith(Arquillian.class)
public class IsMandatoryTest extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return defaultArchive();
    }

    @Test
    public void testPublicIsNonMandatory() throws IOException, SAXException {

        String response = getFromServerPath("public/servlet");

        assertTrue("Resource (Servlet) not invoked, but should have been.", response.contains("Public resource invoked"));
        
        assertTrue("isMandatory should be false for public resource, but was not.",
            response.contains("isMandatory: false"));
    }
    
    @Test
    public void testProtectedIsMandatory() throws IOException, SAXException {

        String response = getFromServerPath("protected/servlet");

        assertTrue("Resource (Servlet) not invoked, but should have been.", response.contains("Resource invoked"));
        
        assertTrue("isMandatory should be true for protected resource, but was not.",
                response.contains("isMandatory: true"));
        
    }


}