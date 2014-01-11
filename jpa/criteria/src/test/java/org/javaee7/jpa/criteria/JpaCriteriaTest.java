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
 * @author Roberto Cortez
 */
@RunWith(Arquillian.class)
public class JpaCriteriaTest {
    @Inject
    private MovieBean movieBean;

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

    @Test
    public void testCriteria() {
        List<Movie> movies = movieBean.listMovies();
        assertEquals(4, movies.size());
        assertTrue(movies.contains(new Movie(1)));
        assertTrue(movies.contains(new Movie(2)));
        assertTrue(movies.contains(new Movie(3)));
        assertTrue(movies.contains(new Movie(4)));

        movieBean.updateMovie();
        movies = movieBean.listMovies();
        assertEquals(4, movies.size());
        assertEquals("INCEPTION", movies.get(2).getName());

        movieBean.deleteMovie();
        movies = movieBean.listMovies();
        assertFalse(movies.isEmpty());
        assertEquals(3, movies.size());
        assertFalse(movies.contains(new Movie(1)));
    }
}
