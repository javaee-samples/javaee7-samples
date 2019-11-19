package org.javaee7.servlet.security.form.based;

import static org.javaee7.ServerOperations.addUsersToContainerIdentityStore;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

/**
 * @author Arun Gupta
 */
@RunWith(Arquillian.class)
public class FormTest {

    private static final String WEBAPP_SRC = "src/main/webapp";

    @ArquillianResource
    private URL base;

    private HtmlForm loginForm;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        
        addUsersToContainerIdentityStore();
        
        return create(WebArchive.class)
            .addClasses(
                SecureServlet.class, 
                LoginServlet.class, 
                ErrorServlet.class)
            
            .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF", "web.xml"))
            .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF", "glassfish-web.xml"));
    }

    @Before
    public void setup() throws IOException {
        @SuppressWarnings("resource")
        WebClient webClient = new WebClient();
        HtmlPage page = webClient.getPage(base + "SecureServlet");
        
        loginForm = page.getForms().get(0);
    }
    
    @After
    public void tearDown() {
        WebClient webClient = loginForm.getPage().getWebClient();
        webClient.getCookieManager().clearCookies();
        webClient.close();
    }

    @Test
    public void testGetWithCorrectCredentials() throws Exception {
        loginForm.getInputByName("j_username").setValueAttribute("u1");
        loginForm.getInputByName("j_password").setValueAttribute("p1");
        HtmlSubmitInput submitButton = loginForm.getInputByName("submitButton");
        HtmlPage page2 = submitButton.click();

        assertEquals("Form-based Security - Success", page2.getTitleText());
    }

    @Test
    public void testGetWithIncorrectCredentials() throws Exception {
        loginForm.getInputByName("j_username").setValueAttribute("random");
        loginForm.getInputByName("j_password").setValueAttribute("random");
        HtmlSubmitInput submitButton = loginForm.getInputByName("submitButton");
        HtmlPage page2 = submitButton.click();

        assertEquals("Form-Based Login Error Page", page2.getTitleText());
    }
}
