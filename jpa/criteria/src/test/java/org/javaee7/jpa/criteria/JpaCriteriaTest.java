package org.javaee7.jpa.criteria;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.*;

/**
 * In this sample we're going to query a simple +JPA Entity+, using the +JPA Criteria API+ and perform a select,
 * update and delete operations.
 *
 * The following +JPA Entity+, represents a Movie which has a name and a comma separated list of actors:
 *
 * include::Movie[]
 *
 * The select, update and delete operations are exposed using a simple stateless ejb.
 *
 * Select every movie:
 * include::MovieBean#listMovies[]
 *
 * Update all the name of the movies to "INCEPTION" where the name of the movie is "Inception":
 * include::MovieBean#updateMovie[]
 *
 * Delete all movies where the name of the movie is "Matrix":
 * include::MovieBean#deleteMovie[]
 *
 * @author Roberto Cortez
 */
@RunWith(Arquillian.class)
public class JpaCriteriaTest {
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
            .addPackage("org.javaee7.jpa.criteria")
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
    public void testCriteria() {
        List<Movie> movies = movieBean.listMovies(); // <1> Get a list of all the movies in the database.
        assertEquals(4, movies.size()); // <2> 4 movies loaded on the db, so the size shoud be 4.
        assertTrue(movies.contains(new Movie(1)));
        assertTrue(movies.contains(new Movie(2)));
        assertTrue(movies.contains(new Movie(3)));
        assertTrue(movies.contains(new Movie(4)));

        movieBean.updateMovie(); // <3> Update name to "INCEPTION" where name is "Inception"
        movies = movieBean.listMovies();
        assertEquals(4, movies.size()); // <4> Size of movies should still be 4.
        assertEquals("INCEPTION", movies.get(2).getName()); // <5> Verify the movie name change.

        movieBean.deleteMovie(); // <6> Now delete the movie "Matrix"
        movies = movieBean.listMovies();
        assertFalse(movies.isEmpty());
        assertEquals(3, movies.size()); // <7> Size of movies should be 3 now.
        assertFalse(movies.contains(new Movie(1))); // <8> Check if the movie "Matrix" is not on the list.
    }
}
