package org.javaee7.jaspic.asyncauthentication;

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
 * 
 * @author Arjan Tijms
 * 
 */
@RunWith(Arquillian.class)
public class AsyncAuthenticationPublicTest extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return defaultArchive();
    }

    /**
     * This tests that an async response works at all in the mere presence of
     * a JASPIC SAM (that does nothing)
     */
    @Test
    public void testBasicAsync() throws IOException, SAXException {

        String response = getFromServerPath("public/asyncServlet");
        assertTrue(response.contains("async response"));
    }

}