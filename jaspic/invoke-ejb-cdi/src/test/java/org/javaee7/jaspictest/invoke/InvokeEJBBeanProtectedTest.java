package org.javaee7.jaspictest.invoke;

import static org.junit.Assert.assertTrue;

import org.javaee7.jaspic.common.ArquillianBase;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This tests that a SAM is able to obtain and call an EJB bean when the request is to a protected resource 
 * (a resource for which security constraints have been set).
 * 
 * @author Arjan Tijms
 * 
 */
@RunWith(Arquillian.class)
public class InvokeEJBBeanProtectedTest extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return defaultArchive();
    }

    @Test
    public void protectedInvokeEJBFromValidateRequest() {
        String response = getFromServerPath("protected/servlet?tech=ejb");
        
        assertTrue(
            "Response did not contain output from EJB bean for validateRequest for protected resource. (note: spec is silent on this, but it should work)", 
            response.contains("validateRequest: Called from EJB")
        );
    }
    
    @Test
    public void protectedInvokeEJBFromCleanSubject() {
        String response = getFromServerPath("protected/servlet?tech=ejb");
        
        assertTrue(
            "Response did not contain output from EJB bean for cleanSubject for protected resource. (note: spec is silent on this, but it should work)", 
            response.contains("cleanSubject: Called from EJB")
        );
    }
    
    @Test
    public void protectedInvokeEJBFromSecureResponse() {
        String response = getFromServerPath("protected/servlet?tech=ejb");
        
        assertTrue(
            "Response did not contain output from EJB bean for secureResponse for protected resource. (note: spec is silent on this, but it should work)", 
            response.contains("secureResponse: Called from EJB")
        );
    }

}