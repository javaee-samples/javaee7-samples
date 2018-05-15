package org.javaee7.jaxrpc.endpoint;

import static org.jboss.shrinkwrap.api.ShrinkWrap.create;

import java.io.File;

//import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class HelloTest {

    private static final String WEBAPP_SRC = "src/main/webapp";

    @ArquillianResource
    private URL url;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        System.out.println("************************************************************");

        WebArchive war =
            create(WebArchive.class)
                .addClasses(HelloService.class, HelloServiceImpl.class)
                .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF/wsdl", "MyHelloService.wsdl"), "wsdl/MyHelloService.wsdl")
                .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF", "mapping.xml"))
                .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF", "webservices.xml"))
                .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF", "web.xml"))
                ;

        System.out.println(war.toString(true));
        System.out.println("************************************************************");

        return war;
    }

    @Before
    public void setupClass() throws MalformedURLException {
    }

    @Test
    public void testHello() throws MalformedURLException {
    }


}
