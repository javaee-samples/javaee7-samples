package org.javaee7.jaspictest.invoke;

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
 * @author Arjan Tijms
 * 
 */
@RunWith(Arquillian.class)
public class InvokeCDIBeanTest extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return tryWrapEAR(
            defaultWebArchive()
                .addAsWebInfResource(resource("beans.xml"))
        );
    }

    @Test
    public void invokeCDIBeanFromSAM() throws IOException, SAXException {

        String response = getFromServerPath("public/servlet?tech=cdi");
        
        assertTrue(
            "Response did not contain output from CDI bean for validateRequest.", 
            response.contains("validateRequest: Called from CDI")
        );
    }
    
    @Test
    public void invokeEJBBeanInValidateRequest() throws IOException, SAXException {

        String response = getFromServerPath("public/servlet?tech=ejb");
        
        assertTrue(
            "Response did not contain output from EJB bean for validateRequest.", 
            response.contains("validateRequest: Called from EJB")
        );
    }
    


}