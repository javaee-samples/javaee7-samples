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
    private WebClient webClient;


    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        
        addUsersToContainerIdentityStore();
        
        return create(WebArchive.class)
            .addAsWebResource(new File(WEBAPP_SRC, "index.jsp"))
            .addAsWebResource(new File(WEBAPP_SRC, "loginerror.jsp"))
            .addAsWebResource(new File(WEBAPP_SRC, "loginform.jsp"))
            .addAsWebResource(new File(WEBAPP_SRC, "form.html"))
            .addAsWebResource(new File(WEBAPP_SRC, "receive.jsp"))
            .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF", "web.xml"))
            .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF", "glassfish-web.xml"));
    }

    @Before
    public void setup() throws IOException {
    	webClient = new WebClient();
    }
    
    @After
    public void tearDown() {
        webClient.getCookieManager().clearCookies();
        webClient.closeAllWindows();
    }

    @Test
    public void testGetWithCorrectCredentials() throws Exception {
    	HtmlPage loginPage = webClient.getPage(base + "/index.jsp");
    	HtmlForm loginForm = loginPage.getForms().get(0);
        loginForm.getInputByName("j_username").setValueAttribute("u1");
        loginForm.getInputByName("j_password").setValueAttribute("p1");
        HtmlSubmitInput submitButton = loginForm.getInputByName("submitButton");
        HtmlPage page2 = submitButton.click();

        assertEquals("Form-based Security - Success", page2.getTitleText());
    }

    @Test
    public void testGetWithIncorrectCredentials() throws Exception {
    	HtmlPage page = webClient.getPage(base + "/index.jsp");
    	HtmlForm loginForm = page.getForms().get(0);
        loginForm.getInputByName("j_username").setValueAttribute("random");
        loginForm.getInputByName("j_password").setValueAttribute("random");
        HtmlSubmitInput submitButton = loginForm.getInputByName("submitButton");
        HtmlPage page2 = submitButton.click();

        assertEquals("Form-Based Login Error Page", page2.getTitleText());
    }
    @Test
    public void testMaintainPostParamsAfterAuth() throws Exception {
        
        String PARAM_VALUE = "example";
        String PARAM_LENGTH = Integer.toString(PARAM_VALUE.length());
    	
        // Unauthenticated page
    	HtmlPage unauthenticatedPage = webClient.getPage(base + "/form.html");
    	HtmlForm unauthenticatedForm = unauthenticatedPage.getForms().get(0);
    	unauthenticatedForm.getInputByName("name").setValueAttribute(PARAM_VALUE);
    	HtmlSubmitInput unauthenticatedSubmitButton = unauthenticatedForm.getInputByValue("Submit");
    	
    	// we request an protected page, so we are presented the login page.    	
    	HtmlPage loginPage = unauthenticatedSubmitButton.click();
    	HtmlForm loginForm = loginPage.getForms().get(0);
        loginForm.getInputByName("j_username").setValueAttribute("u1");
        loginForm.getInputByName("j_password").setValueAttribute("p1");
        HtmlSubmitInput submitButton = loginForm.getInputByName("submitButton");
        
        HtmlPage receivePage = submitButton.click();        
        assertEquals(PARAM_LENGTH, receivePage.getElementById("paramLength").getTextContent());
        assertEquals(PARAM_LENGTH, receivePage.getElementById("arrayLength").getTextContent());
        assertEquals(PARAM_VALUE, receivePage.getElementById("param").getTextContent());
    }
}
