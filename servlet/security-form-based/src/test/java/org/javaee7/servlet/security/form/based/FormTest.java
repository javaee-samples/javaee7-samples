package org.javaee7.servlet.security.form.based;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.runner.RunWith;

/**
 * @author Arun Gupta
 */
@RunWith(Arquillian.class)
public class FormTest {

    private static final String WEBAPP_SRC = "src/main/webapp";

    @ArquillianResource
    private URL base;

    HtmlForm loginForm;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
            .addAsWebResource(new File(WEBAPP_SRC, "index.jsp"))
            .addAsWebResource(new File(WEBAPP_SRC, "loginerror.jsp"))
            .addAsWebResource(new File(WEBAPP_SRC, "loginform.jsp"))
            .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF", "web.xml"))
            .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF", "glassfish-web.xml"));
    }

    @Before
    public void setup() throws IOException {
        WebClient webClient = new WebClient();
        HtmlPage page = webClient.getPage(base + "/index.jsp");
        loginForm = page.getForms().get(0);

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
