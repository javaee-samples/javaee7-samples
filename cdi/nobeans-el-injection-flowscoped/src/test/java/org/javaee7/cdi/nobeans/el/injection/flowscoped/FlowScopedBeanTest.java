package org.javaee7.cdi.nobeans.el.injection.flowscoped;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.File;
import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.*;

/**
 * @author Arun Gupta
 */
@RunWith(Arquillian.class)
public class FlowScopedBeanTest {

    private static final String WEBAPP_SRC = "src/main/webapp";

    @ArquillianResource
    private URL base;

    @Deployment(testable = false)
    public static WebArchive deploy() {
        return ShrinkWrap.create(WebArchive.class)
            .addClass(FlowScopedBean.class)
            .addAsWebInfResource((new File(WEBAPP_SRC + "/WEB-INF", "web.xml")))
            .addAsWebResource((new File(WEBAPP_SRC, "myflow/myflow-flow.xml")), "myflow/myflow-flow.xml")
            .addAsWebResource((new File(WEBAPP_SRC, "myflow/index.xhtml")), "myflow/index.xhtml");
    }

    @Test
    public void checkRenderedPage() throws Exception {
        WebClient webClient = new WebClient();
        HtmlPage page = webClient.getPage(base + "/faces/myflow/index.xhtml");
        assertNotNull(page);
        assert (page.asText().contains("Hello there!"));
    }
}
