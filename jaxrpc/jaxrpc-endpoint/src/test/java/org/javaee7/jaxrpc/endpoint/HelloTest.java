package org.javaee7.jaxrpc.endpoint;

//import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class HelloTest {

    @ArquillianResource
    private URL url;
   
    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class).
            addPackage("org.javaee7.jaxrpc.endpoint");
    }

    @Before
    public void setupClass() throws MalformedURLException {
    }

    @Test
    public void testHello() throws MalformedURLException {
    }

   
}
