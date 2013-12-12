package org.javaee7.servlet.security.basicauth.omission;

import com.meterware.httpunit.AuthorizationRequiredException;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import java.io.File;
import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

/**
 * @author Arun Gupta
 */
@RunWith(Arquillian.class)
public class SecureServletTest {
    
    private static final String WEBAPP_SRC = "src/main/webapp";

    @ArquillianResource
    private URL base;
    
    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class).
                addClass(SecureServlet.class).
                addAsWebInfResource((new File(WEBAPP_SRC + "/WEB-INF", "web.xml")));
        return war;
    }

    @Test
    public void testGetWithCorrectCredentials() throws Exception {
        WebConversation conv = new WebConversation();
        conv.setAuthentication("file", "u1", "p1");
        GetMethodWebRequest getRequest = new GetMethodWebRequest(base + "/SecureServlet");
        WebResponse response = null;
        try {
            response = conv.getResponse(getRequest);
        } catch (AuthorizationRequiredException e) {
            fail(e.getMessage());
        }
        assertNotNull(response);
        assertTrue(response.getText().contains("<title>Servlet Security - Basic Auth with File-base Realm</title>"));
    }

    @Test
    public void testGetWithIncorrectCredentials() throws Exception {
        WebConversation conv = new WebConversation();
        conv.setAuthentication("file", "u", "p1");
        GetMethodWebRequest getRequest = new GetMethodWebRequest(base + "/SecureServlet");
        try {
            conv.getResponse(getRequest);
        } catch (AuthorizationRequiredException e) {
            assertNotNull(e);
            return;
        }
        fail("/SecureServlet could be accessed without proper security credentials");
    }    

    @Test
    public void testSPostWithNoCredentials() throws Exception {
        WebConversation conv = new WebConversation();
//        conv.setAuthentication("file", "u1", "p1");
        PostMethodWebRequest getRequest = new PostMethodWebRequest(base + "/SecureServlet");
        WebResponse response = null;
        try {
            response = conv.getResponse(getRequest);
        } catch (AuthorizationRequiredException e) {
            fail(e.getMessage());
        }
        assertNotNull(response);
        assertTrue(response.getText().contains("<title>Servlet Security - Basic Auth with File-base Realm</title>"));
    }

    @Test
    public void testPostWithCorrectCredentials() throws Exception {
        WebConversation conv = new WebConversation();
        conv.setAuthentication("file", "u1", "p1");
        PostMethodWebRequest getRequest = new PostMethodWebRequest(base + "/SecureServlet");
        WebResponse response = null;
        try {
            response = conv.getResponse(getRequest);
        } catch (AuthorizationRequiredException e) {
            fail(e.getMessage());
        }
        assertNotNull(response);
        assertTrue(response.getText().contains("<title>Servlet Security - Basic Auth with File-base Realm</title>"));
    }
    

    @Test
    public void testPostWithIncorrectCredentials() throws Exception {
        WebConversation conv = new WebConversation();
        conv.setAuthentication("file", "random", "random");
        PostMethodWebRequest getRequest = new PostMethodWebRequest(base + "/SecureServlet");
        WebResponse response = null;
        try {
            response = conv.getResponse(getRequest);
        } catch (AuthorizationRequiredException e) {
            fail(e.getMessage());
        }
        assertNotNull(response);
        assertTrue(response.getText().contains("<title>Servlet Security - Basic Auth with File-base Realm</title>"));
    }
    
}
