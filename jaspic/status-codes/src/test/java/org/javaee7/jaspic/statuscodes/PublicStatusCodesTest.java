package org.javaee7.jaspic.statuscodes;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.javaee7.jaspic.common.ArquillianBase;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * This tests that a SAM can set a 404 response code when a public resource is requested.
 * Note the resource is not actual invoked, as the SAM returns SEND_FAILURE.
 * 
 * @author Arjan Tijms
 * 
 */
@RunWith(Arquillian.class)
public class PublicStatusCodesTest extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return defaultArchive();
    }

    @Test
    public void test404inResponse() throws IOException  {

        int code = getWebClient().getPage(getBase() + "public/servlet")
                                 .getWebResponse()
                                 .getStatusCode();
        
        assertEquals(
            "Response should have 404 not found as status code, but did not.", 
            404, code
        );
    }

}