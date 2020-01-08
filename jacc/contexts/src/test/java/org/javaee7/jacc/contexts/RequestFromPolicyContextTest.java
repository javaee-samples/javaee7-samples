package org.javaee7.jacc.contexts;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.javaee7.jacc.contexts.bean.JaccRequestBean;
import org.javaee7.jacc.contexts.sam.SamAutoRegistrationListener;
import org.javaee7.jacc.contexts.sam.TestServerAuthModule;
import org.javaee7.jacc.contexts.servlet.RequestServlet;
import org.javaee7.jacc.contexts.servlet.RequestServletEJB;
import org.javaee7.jacc.contexts.servlet.SubjectServlet;
import org.javaee7.jaspic.common.ArquillianBase;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xml.sax.SAXException;

/**
 * This tests demonstrates how code can obtain a reference to the {@link HttpServletRequest} from the JACC
 * context.
 * 
 * @author Arjan Tijms
 * 
 */
@RunWith(Arquillian.class)
public class RequestFromPolicyContextTest extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        WebArchive archive = ((WebArchive) ArquillianBase.defaultArchive())
                .addClasses(
                    SamAutoRegistrationListener.class, TestServerAuthModule.class,
                    RequestServlet.class, SubjectServlet.class);
        
        if (!Boolean.valueOf(System.getProperty("skipEJB"))) {
            archive.addClasses(JaccRequestBean.class, RequestServletEJB.class);
        } else {
            System.out.println("Skipping EJB based tests");
        }
        
        return archive;
    }

    /**
     * Tests that we are able to obtain a reference to the {@link HttpServletRequest} from a Servlet.
     */
    @Test
    public void testCanObtainRequestInServlet() throws IOException, SAXException {

        String response = getFromServerPath("requestServlet");

        assertTrue(response.contains("Obtained request from context."));
    }
    

    /**
     * Tests that the {@link HttpServletRequest} reference that we obtained from JACC in a Servlet actually
     * works by getting a request attribute and request parameter from it.
     */
    @Test
    public void testDataInServlet() throws IOException, SAXException {

        String response = getFromServerPath("requestServlet?jacc_test=true");

        assertTrue(
            "Request scope attribute not present in request obtained from context in Servlet, but should have been",
            response.contains("Attribute present in request from context."));

        assertTrue(
            "Request parameter not present in request obtained from context in Servlet, but should have been",
            response.contains("Request parameter present in request from context."));
    }

    /**
     * Tests that the {@link HttpServletRequest} reference that we obtained from JACC in an EJB actually
     * works by getting a request attribute and request parameter from it.
     */
    @Test
    public void testDataInEJB() throws IOException, SAXException {
        
        Assume.assumeTrue(false);

        String response = getFromServerPath("requestServlet?jacc_test=true");

        assertTrue(
            "Request scope attribute not present in request obtained from context in EJB, but should have been",
            response.contains("Attribute present in request from context."));

        assertTrue(
            "Request parameter not present in request obtained from context in EJB, but should have been",
            response.contains("Request parameter present in request from context."));
    }
    
    /**
     * Tests that we are able to obtain a reference to the {@link HttpServletRequest} from an EJB.
     */
    @Test
    public void testCanObtainRequestInEJB() throws IOException, SAXException {
        
        Assume.assumeTrue(false);

        String response = getFromServerPath("requestServletEJB");

        assertTrue(response.contains("Obtained request from context."));
    }

}