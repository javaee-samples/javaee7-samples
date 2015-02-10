package org.javaee7.jaxws.client;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.QName;

import org.javaee7.jaxws.client.gen.EBook;
import org.javaee7.jaxws.client.gen.EBookStore;
import org.javaee7.jaxws.client.gen.EBookStoreImplService;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.resolver.api.maven.archive.importer.MavenImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;

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
public class EBookStoreClientSampleTest {
    private static EBookStoreImplService eBookStoreService;

    /**
     * Method for creating and deploying the war file from 'jaxws-endpoint' project,
     * which contains the web service to be tested.
     *
     * @return a war file
     */
    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(MavenImporter.class)
            .loadPomFromFile("../jaxws-endpoint/pom.xml")
            .importBuildOutput()
            .as(WebArchive.class);
    }

    @ArquillianResource
    private URL url;

    @Before
    public void setUp() throws Exception {
        eBookStoreService = new EBookStoreImplService(
            new URL(url, "EBookStoreImplService?wsdl"),
            new QName("http://endpoint.jaxws.javaee7.org/", "EBookStoreImplService"));
    }

    @Test
    public void test1WelcomeMessage() throws MalformedURLException {
        EBookStore eBookStore = eBookStoreService.getEBookStoreImplPort();
        String response = eBookStore.welcomeMessage("Jackson");
        assertEquals("Welcome to EBookStore WebService, Mr/Mrs Jackson", response);
    }

    @Test
    public void test2SaveAndTakeBook() throws MalformedURLException {
        EBookStore eBookStore = eBookStoreService.getPort(EBookStore.class);

        EBook eBook = new EBook();
        eBook.setTitle("The Jungle Book");
        eBook.setNumPages(225);
        eBook.setPrice(17.9);
        eBookStore.saveBook(eBook);
        eBook = new EBook();

        eBook.setTitle("Animal Farm");
        eBook.setNumPages(113);
        eBook.setPrice(22.5);
        List<String> notes = Arrays.asList(new String[] { "Great book", "Not too bad" });
        eBook.getNotes().addAll(notes);
        eBookStore.saveBook(eBook);

        EBook response = eBookStore.takeBook("Animal Farm");
        assertEquals(eBook.getNumPages(), response.getNumPages());
        assertEquals(eBook.getPrice(), response.getPrice(), 0);
        assertEquals(eBook.getTitle(), response.getTitle());
        assertEquals(notes, response.getNotes());

    }

}
