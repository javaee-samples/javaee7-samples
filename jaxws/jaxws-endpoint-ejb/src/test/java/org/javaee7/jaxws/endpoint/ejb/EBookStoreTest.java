/** Copyright Payara Services Limited **/
package org.javaee7.jaxws.endpoint.ejb;

import static org.junit.Assert.assertEquals;
import static org.junit.runners.MethodSorters.NAME_ASCENDING;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.javaee7.jaxws.endpoint.ejb.EBookStore;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Fermin Gallego
 * @author Arjan Tijms
 */
@RunWith(Arquillian.class)
@FixMethodOrder(NAME_ASCENDING)
public class EBookStoreTest {
    
    @ArquillianResource
    private URL url;
    
    private URL rootUrl;

    private static Service eBookStoreService;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class).
            addPackage("org.javaee7.jaxws.endpoint.ejb");
    }

    @Before
    public void setupClass() throws MalformedURLException {
        rootUrl = new URL(url.getProtocol(), url.getHost(), url.getPort(), "");
        
        eBookStoreService = Service.create(
            // The WSDL file used to create this service is fetched from the application we deployed
            // above using the createDeployment() method.
            new URL(rootUrl, "EBookStoreImplService/EBookStoreImpl?wsdl"),
            new QName("http://ejb.endpoint.jaxws.javaee7.org/", "EBookStoreImplService"));
    }

    @Test
    public void test1WelcomeMessage() throws MalformedURLException {
        assertEquals(
            "Welcome to EBookStore WebService, Mr/Mrs Johnson", 
            eBookStoreService.getPort(EBookStore.class).welcomeMessage("Johnson"));
    }

   
}
