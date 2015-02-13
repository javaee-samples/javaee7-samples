package org.javaee7.jaxws.endpoint;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.javaee7.jaxws.endpoint.EBook;
import org.javaee7.jaxws.endpoint.EBookStore;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

/**
 * @author Fermin Gallego
 */
@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EBookStoreTest {

    private static Service eBookStoreService;

    /**
     * Arquillian specific method for creating a file which can be deployed
     * while executing the test.
     *
     * @return a war file
     */
    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class).
            addPackage("org.javaee7.jaxws.endpoint");
    }

    @ArquillianResource
    private URL url;

    @Before
    public void setupClass() throws MalformedURLException {
        eBookStoreService = Service.create(
            new URL(url, "EBookStoreImplService?wsdl"),
            new QName("http://endpoint.jaxws.javaee7.org/", "EBookStoreImplService"));
    }

    @Test
    public void test1WelcomeMessage() throws MalformedURLException {
        EBookStore eBookStore = eBookStoreService.getPort(EBookStore.class);
        String response = eBookStore.welcomeMessage("Johnson");
        assertEquals("Welcome to EBookStore WebService, Mr/Mrs Johnson", response);
    }

    @Test
    public void test2SaveAndTakeBook() throws MalformedURLException {
        EBookStore eBookStore = eBookStoreService.getPort(EBookStore.class);
        EBook eBook = new EBook();
        eBook.setTitle("The Lord of the Rings");
        eBook.setNumPages(1178);
        eBook.setPrice(21.8);
        eBookStore.saveBook(eBook);
        eBook = new EBook();

        eBook.setTitle("Oliver Twist");
        eBook.setNumPages(268);
        eBook.setPrice(7.45);
        eBookStore.saveBook(eBook);
        EBook response = eBookStore.takeBook("Oliver Twist");

        assertEquals(eBook.getNumPages(), response.getNumPages());
    }

    @Test
    public void test3FindEbooks() {
        EBookStore eBookStore = eBookStoreService.getPort(EBookStore.class);
        List<String> titleList = eBookStore.findEBooks("Rings");

        assertNotNull(titleList);
        assertEquals(1, titleList.size());
        assertEquals("The Lord of the Rings", titleList.get(0));
    }

    @Test
    public void test4AddAppendix() {
        EBookStore eBookStore = eBookStoreService.getPort(EBookStore.class);
        EBook eBook = eBookStore.takeBook("Oliver Twist");

        assertEquals(268, eBook.getNumPages());
        EBook eBookResponse = eBookStore.addAppendix(eBook, 5);

        assertEquals(268, eBook.getNumPages());
        assertEquals(273, eBookResponse.getNumPages());
    }
}
