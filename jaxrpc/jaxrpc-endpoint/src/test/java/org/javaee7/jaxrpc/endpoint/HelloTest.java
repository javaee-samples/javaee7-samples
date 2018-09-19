package org.javaee7.jaxrpc.endpoint;

import static javax.xml.rpc.Call.SOAPACTION_URI_PROPERTY;
import static javax.xml.rpc.Call.SOAPACTION_USE_PROPERTY;
import static javax.xml.rpc.ParameterMode.IN;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;
import javax.xml.rpc.Call;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.ServiceFactory;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class HelloTest {

    private static final String WEBAPP_SRC = "src/main/webapp";
    private static final String ENCODING_STYLE_PROPERTY = "javax.xml.rpc.encodingstyle.namespace.uri";
    private static final String NS_XSD = "http://www.w3.org/2001/XMLSchema";

    @ArquillianResource
    private URL url;


    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        System.out.println("************** DEPLOYING ************************************");

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

    @Test
    @RunAsClient
    public void testHelloProxy() throws MalformedURLException, ServiceException, RemoteException {
        HelloService helloService = (HelloService)
                ServiceFactory.newInstance()
                              .createService(
                                  new URL(url, "hello?wsdl"),
                                  new QName("urn:sample", "MyHelloService"))
                              .getPort(
                                  new QName("urn:sample", "HelloServicePort"),
                                  HelloService.class);

        String result = helloService.sayHello("Sailor");

        assertEquals("Hello Sailor", result);
    }

    @Test
    @RunAsClient
    public void testHelloDII() throws MalformedURLException, ServiceException, RemoteException {
        Call call = ServiceFactory.newInstance()
                                  .createService(new QName("MyHelloService"))
                                  .createCall(new QName("HelloServicePort"));

        call.setTargetEndpointAddress(url + "hello");

        call.setProperty(SOAPACTION_USE_PROPERTY, true);
        call.setProperty(SOAPACTION_URI_PROPERTY, "");
        call.setProperty(ENCODING_STYLE_PROPERTY, "http://schemas.xmlsoap.org/soap/encoding/");

        call.setReturnType(new QName(NS_XSD, "string"));
        call.setOperationName(new QName("urn:sample", "sayHello"));
        call.addParameter("String_1", new QName(NS_XSD, "string"), IN);

        String result = (String) call.invoke(new String[] { "Captain" });

        assertEquals("Hello Captain", result);
    }




}
