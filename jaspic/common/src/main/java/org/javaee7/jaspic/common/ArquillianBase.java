package org.javaee7.jaspic.common;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * 
 * @author Arjan Tijms
 * 
 */
public class ArquillianBase {

    private static final String WEBAPP_SRC = "src/main/webapp";
    private WebClient webClient;

    public static WebArchive defaultArchive() {
        return ShrinkWrap.create(WebArchive.class)
                         .addPackages(true, "org.javaee7.jaspic")
                         .addAsWebInfResource(resource("web.xml"))
                         .addAsWebInfResource(resource("jboss-web.xml"))
                         .addAsWebInfResource(resource("glassfish-web.xml"));
    }

    private static File resource(String name) {
        return new File(WEBAPP_SRC + "/WEB-INF", name);
    }
    
    @ArquillianResource
    private URL base;
    
    

    @Before
    public void setUp() {
        webClient = new WebClient();
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
    }

    @After
    public void tearDown() {
        webClient.getCookieManager().clearCookies();
        webClient.closeAllWindows();
    }
    
    protected WebClient getWebClient() {
        return webClient;
    }
    
    protected URL getBase() {
        return base;
    }
    
    /**
     * Gets content from the path that's relative to the base URL on which the Arquillian test
     * archive is deployed.
     * 
     * @param path the path relative to the URL on which the Arquillian test is deployed
     * @return the raw content as a string as returned by the server
     */
    protected String getFromServerPath(final String path) {
        try {
            return webClient.getPage(base + path).getWebResponse().getContentAsString();
        } catch (FailingHttpStatusCodeException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
