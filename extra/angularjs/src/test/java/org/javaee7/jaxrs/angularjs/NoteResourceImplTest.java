package org.javaee7.jaxrs.angularjs;

import com.example.domain.Note;
import com.example.rest.NoteApp;
import com.example.rest.NoteResource;
import com.example.rest.NoteResourceImpl;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.GrapheneRuntime;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.assertEquals;

@Cleanup(phase = TestExecutionPhase.BEFORE)
@UsingDataSet
@RunWith(Arquillian.class)
public class NoteResourceImplTest {

    @Deployment
    public static Archive createDeployment()
    {
        final GenericArchive webResources = ShrinkWrap.create(GenericArchive.class)
            .as(ExplodedImporter.class)
            .importDirectory("src/main/webapp")
            .as(GenericArchive.class);
        final File[] seleniumApi = Maven.resolver()
            .loadPomFromFile("pom.xml")
            .resolve("org.seleniumhq.selenium:selenium-api:2.35.0")
            .withTransitivity()
            .asFile();
        return ShrinkWrap.create(WebArchive.class, NoteResourceImplTest.class.getSimpleName() + ".war")
            .addClasses(Note.class, NoteApp.class, NoteResource.class, NoteResourceImpl.class)
            .addAsResource("META-INF/persistence.xml")
            .addAsWebInfResource("enforce-beans.xml", "jboss-all.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsLibraries(seleniumApi)
            .merge(webResources);
    }

    /**
     * See https://issues.jboss.org/browse/ARQ-1077
     */
    @Test
    @InSequence(1)
    public void setupDB_ARQ1077_Workaround()
    {
    }

    @Test
    @InSequence(3)
    public void setupDB_ARQ1077_Workaround_2()
    {
    }

    @Test
    @InSequence(5)
    public void setupDB_ARQ1077_Workaround_3()
    {
    }

    @InSequence(4)
    @RunAsClient
    @Test
    public void addNewNote(@ArquillianResource URL deploymentURL, @Drone WebDriver driver) throws Exception
    {
        //        Given
        driver.navigate().to(deploymentURL);
        final TodoPage page = GrapheneRuntime.getInstance().goTo(TodoPage.class);
        assertEquals(3, page.getTodos().size());
        //        When
        page.addNote();
        page.typeTitle("New title");
        page.typeSummary("New summary");
        page.save();
        //        Then
        assertEquals(4, page.getTodos().size());
        assertEquals("New title", page.getTodos().get(3).getTitle());
        assertEquals("New summary", page.getTodos().get(3).getSummary());
    }

    @InSequence(2)
    @RunAsClient
    @Test
    public void onenterContainsNotesFromDB(@ArquillianResource URL deploymentURL, @Drone WebDriver driver)
    {
        //        Given
        //        When
        driver.navigate().to(deploymentURL);
        final TodoPage page = GrapheneRuntime.getInstance().goTo(TodoPage.class);
        //        Then
        assertEquals(3, page.getTodos().size());
        assertEquals("Note A", page.getTodos().get(0).getTitle());
        assertEquals("Note B", page.getTodos().get(1).getTitle());
        assertEquals("Note C", page.getTodos().get(2).getTitle());
    }

    @InSequence(6)
    @RunAsClient
    @Test
    public void removeNote(@ArquillianResource URL deploymentURL, @Drone WebDriver driver) throws Exception
    {
        //        Given
        driver.navigate().to(deploymentURL);
        final TodoPage page = GrapheneRuntime.getInstance().goTo(TodoPage.class);
        assertEquals(3, page.getTodos().size());
        //        When
        page.getTodos().get(0).remove();
        //        Then
        assertEquals("Note B", page.getTodos().get(0).getTitle());
        assertEquals("Note C", page.getTodos().get(1).getTitle());
    }
}
