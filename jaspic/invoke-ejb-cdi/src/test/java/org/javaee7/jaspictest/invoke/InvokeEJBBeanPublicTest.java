package org.javaee7.jaspictest.invoke;

import static org.junit.Assert.assertTrue;

import org.javaee7.jaspic.common.ArquillianBase;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This tests that a SAM is able to obtain and call an EJB bean when the request is to a public resource 
 * (a resource for which no security constraints have been set).
 * 
 * @author Arjan Tijms
 * 
 */
@RunWith(Arquillian.class)
public class InvokeEJBBeanPublicTest extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return defaultArchive();
    }

    @Test
    public void publicInvokeEJBFromValidateRequest() {
        String response = getFromServerPath("public/servlet?tech=ejb");
        
        assertTrue(
            "Response did not contain output from EJB bean for validateRequest for public resource.", 
            response.contains("validateRequest: Called from EJB")
        );
    }
    
    @Test
    public void publicInvokeEJBFromCleanSubject() {
        String response = getFromServerPath("public/servlet?tech=ejb");
        
        assertTrue(
            "Response did not contain output from EJB bean for cleanSubject for public resource.", 
            response.contains("cleanSubject: Called from EJB")
        );
    }
    
    @Test
    public void publicInvokeEJBFromSecureResponse() {
        String response = getFromServerPath("public/servlet?tech=ejb");
        
        assertTrue(
            "Response did not contain output from EJB bean for secureResponse for public resource.", 
            response.contains("secureResponse: Called from EJB")
        );
    }

}