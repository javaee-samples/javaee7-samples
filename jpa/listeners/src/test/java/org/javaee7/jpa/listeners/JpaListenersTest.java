package org.javaee7.jpa.listeners;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.javaee7.jpa.listeners.MovieListener.*;
import static org.junit.Assert.*;

/**
 * In this sample we're going to query a simple +JPA Entity+, using the +JPA EntityManager+ and perform a findAll,
 * persist, merge, and remove operations. By calling these operations we want to demonstrate the behaviour of +Entity
 * Listener Methods+: +@PostLoad+, +@PrePersist+, +@PostPersist+, +@PreUpdate+, +@PostUpdate+, +@PreRemove+,
 * +@PostRemove+ defined on +MovieListener+:
 *
 * include::MovieListener[]
 *
 * The following +JPA Entity+, represents a Movie which has a name and a comma separated list of actors:
 *
 * include::Movie[]
 *
 *
 * @author Roberto Cortez
 */
@RunWith(Arquillian.class)
public class JpaListenersTest {
    @Inject
    private MovieBean movieBean;

    /**
     * We're just going to deploy the application as a +web archive+. Note the inclusion of the following files:
     *
     * [source,file]
     * ----
     * /META-INF/persistence.xml
     * /META-INF/create.sql
     * /META-INF/drop.sql
     * /META-INF/load.sql
     * ----
     *
     * The +persistence.xml+ file is needed of course for the persistence unit definition. A datasource is not
     * needed, since we can now use the new default datasource available in +JEE7+. We're also using the new
     * +javax.persistence.schema-generation.*+ propertires to create, populate and drop the database.
     */
    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class)
            .addPackage("org.javaee7.jpa.listeners")
            .addAsResource("META-INF/persistence.xml")
            .addAsResource("META-INF/create.sql")
            .addAsResource("META-INF/drop.sql")
            .addAsResource("META-INF/load.sql");
        System.out.println(war.toString(true));
        return war;
    }

    /**
     * In the test, we're just going to invoke the different operations in sequence keeping in mind that each
     * invocation might be dependent of the previous invoked operation.
     */
    @Test
    public void testListeners() throws Exception {
        List<Movie> movies = movieBean.listMovies(); // <1> 4 movies in the database, so +@PostLoad+ method called 4x
        assertEquals(4, movies.size());

        assertFalse(prePersistInvoked);
        assertFalse(postPersistInvoked);
        movieBean.createMovie(); // <2> On persist both +@PrePersist+ and +@PostPersist+ are called
        assertTrue(prePersistInvoked);
        assertTrue(postPersistInvoked);

        movies = movieBean.listMovies(); // <3> 5 movies now, so +@PostLoad+ method called 5x
        assertEquals(5, movies.size());
        assertTrue(movies.contains(new Movie(5)));

        assertFalse(preUpdateInvoked);
        assertFalse(postUpdateInvoked);
        movieBean.updateMovie(); // <4> On merge both +@PreUpdate+ and +@PostUpdate+ are called
        assertTrue(preUpdateInvoked);
        assertTrue(postUpdateInvoked);

        movies = movieBean.listMovies(); // <5> Still 5 mpvies, so +@PostLoad+ method called again 5x
        assertEquals(5, movies.size());
        assertEquals("Inception2", movies.get(2).getName());

        assertFalse(preRemoveInvoked);
        assertFalse(postRemoveInvoked);
        movieBean.deleteMovie(); // <6> On remove both +@PreRemove+ and +@PostRemove+ are called
        assertTrue(preRemoveInvoked);
        assertTrue(postRemoveInvoked);

        movies = movieBean.listMovies(); // <7> 4 movies now, so +@PostLoad+ method called 4x
        assertFalse(movies.isEmpty());
        assertEquals(4, movies.size());
        assertFalse(movies.contains(new Movie(3)));

        assertTrue(MovieListener.entityListenersCountDownLatch.await(0, TimeUnit.SECONDS));
    }
}
