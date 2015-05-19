package org.javaee7.jaspic.common;

import static java.lang.Boolean.getBoolean;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
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
    
    public static Archive<?> defaultArchive() {
        return tryWrapEAR(defaultWebArchive());
    }
    
    public static WebArchive defaultWebArchive() {
        return 
            create(WebArchive.class, "test.war")
                .addPackages(true, "org.javaee7.jaspic")
                .deleteClass(ArquillianBase.class)
                .addAsWebInfResource(resource("web.xml"))
                .addAsWebInfResource(resource("jboss-web.xml"))
                .addAsWebInfResource(resource("glassfish-web.xml"));
    }
    
    public static Archive<?> tryWrapEAR(WebArchive webArchive) {
        if (getBoolean("useEarForJaspic")) {
            return
                // EAR archive
                create(EnterpriseArchive.class, "test.ear")
                
                    // Liberty needs to have the binding file in an ear.
                    .addAsManifestResource(resource("ibm-application-bnd.xml"))
    
                    // Web module
                    // This is needed to prevent Arquillian generating an illegal application.xml
                    .addAsModule(
                        webArchive
                    );  
        } else {
            return webArchive;
        }
    }

    public static File resource(String name) {
        return new File(WEBAPP_SRC + "/WEB-INF", name);
    }
    
    public static File web(String name) {
        return new File(WEBAPP_SRC, name);
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
