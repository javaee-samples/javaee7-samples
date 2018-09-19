/** Copyright Payara Services Limited **/
package org.javaee7.jaxrpc.security;

import static javax.xml.rpc.Stub.ENDPOINT_ADDRESS_PROPERTY;
import static javax.xml.rpc.Stub.PASSWORD_PROPERTY;
import static javax.xml.rpc.Stub.USERNAME_PROPERTY;
import static org.javaee7.ServerOperations.addUsersToContainerIdentityStore;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.Stub;

import org.javaee7.jaxrpc.security.HelloService;
import org.javaee7.jaxrpc.security.HelloServiceImpl;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import stub.MyHelloService_Impl;


/**
 * This test demonstrates doing a SOAP request using client side generated stubs to a remote
 * JAX-RPC SOAP service that is protected by an authentication mechanism that requires an
 * encrypted username/password credential.
 * 
 * @author Arjan Tijms
 * 
 */
@RunWith(Arquillian.class)
public class HelloTest {

    private static final String WEBAPP_SRC = "src/main/webapp";

    @ArquillianResource
    private URL url;


    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        System.out.println("************** DEPLOYING ************************************");
        
        System.out.println("Adding test user u1 with group g1");
        
        addUsersToContainerIdentityStore();

        WebArchive war =
            create(WebArchive.class)
                .addClasses(HelloService.class, HelloServiceImpl.class)
                
                // The wsdl describes the HelloService.class in xml. The .wsdl is generated from HelloService by the wscompile tool
                // (see build.xml).
                .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF/wsdl", "MyHelloService.wsdl"), "wsdl/MyHelloService.wsdl")
                
                // The mapping.xml more precisely describes the HelloService.class in xml. 
                // It's also generated from it by the wscompile tool
                .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF", "mapping.xml"))
                
                // webservices.xml is the entry file for webservices that links to the .wsdl and mapping.xml
                // mentioned above, and to a (virtual) servlet class.
                .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF", "webservices.xml"))
                
                // Maps the (virtual) servlet class introduced in webservices.xml to a URL pattern
                // This thus effectively gives the webservice a path, e.g. localhost:8080/ourapp/path.
                .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF", "web.xml"))
                
                // Maps (in a SUN specific way) SOAP security constraints to the webservice.
                .addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF", "sun-web.xml"))
                ;

        System.out.println(war.toString(true));
        System.out.println("************************************************************");

        return war;
    }

    @Test
    @RunAsClient
    public void testHelloStaticStub() throws MalformedURLException, ServiceException, RemoteException {
        
        stub.HelloService helloService = new MyHelloService_Impl().getHelloServicePort();
        
        ((Stub) helloService)._setProperty(USERNAME_PROPERTY, "u1");
        ((Stub) helloService)._setProperty(PASSWORD_PROPERTY, "p1");
        ((Stub) helloService)._setProperty(ENDPOINT_ADDRESS_PROPERTY, url + "hello");

        String result = helloService.sayHello("Sailor");

        assertEquals("Hello Sailor", result);
    }

}
