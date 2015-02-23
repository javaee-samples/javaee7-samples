package org.javaee7.cdi.nobeans.el.injection;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import java.io.File;
import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 * @author Arun Gupta
 */
@RunWith(Arquillian.class)
public class ScopedBeanTest {

    private static final String WEBAPP_SRC = "src/main/webapp";

    @ArquillianResource
    private URL base;

    @Deployment(testable = false)
    public static WebArchive deploy() {
        return ShrinkWrap.create(WebArchive.class)
            .addClass(ScopedBean.class)
            .addAsWebInfResource((new File(WEBAPP_SRC + "/WEB-INF", "web.xml")))
            .addAsWebResource((new File(WEBAPP_SRC, "index.xhtml")));
    }

    @Test
    public void checkRenderedPage() throws Exception {
        WebConversation conv = new WebConversation();
        GetMethodWebRequest getRequest = new GetMethodWebRequest(base + "/faces/index.xhtml");
        String responseText = conv.getResponse(getRequest).getText();
        assert (responseText.contains("Hello there!"));
    }
}
