package org.javaee7.servlet.security.deny.uncovered;

import com.meterware.httpunit.AuthorizationRequiredException;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.HttpException;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.PutMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import java.io.ByteArrayInputStream;
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
    public void testGetMethod() throws Exception {
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
    public void testPostMethod() throws Exception {
        WebConversation conv = new WebConversation();
        conv.setAuthentication("file", "u1", "p1");
        
        PostMethodWebRequest postRequest = new PostMethodWebRequest(base + "/SecureServlet");
        try {
            conv.getResponse(postRequest);
        } catch (HttpException e) {
            assertEquals(403, e.getResponseCode());
            return;
        }
        fail("POST method could be called");
    }

    @Test
    public void testPutMethod() throws Exception {
        WebConversation conv = new WebConversation();
        conv.setAuthentication("file", "u1", "p1");
        
        byte[] bytes = new byte[10];
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        PutMethodWebRequest putRequest = new PutMethodWebRequest(base + "/SecureServlet", bais, "text/plain");
        try {
            conv.getResponse(putRequest);
        } catch (HttpException e) {
            assertEquals(403, e.getResponseCode());
            return;
        }
        fail("PUT method could be called");
    }
}
