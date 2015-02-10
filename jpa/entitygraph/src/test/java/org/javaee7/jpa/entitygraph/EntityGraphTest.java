package org.javaee7.jpa.entitygraph;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnitUtil;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * In this sample we're going to query a +JPA Entity+ and control property loading by providing +Hints+ using the new
 * +JPA Entity Graph+ API.
 * <p/>
 * Entity Graphs are used in the specification of fetch plans for query or find operations.
 *
 * @author Roberto Cortez
 */
@RunWith(Arquillian.class)
public class EntityGraphTest {
    @PersistenceContext
    private EntityManager entityManager;
    @Inject
    private MovieBean movieBean;

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class)
            .addPackage("org.javaee7.jpa.entitygraph")
            .addAsResource("META-INF/persistence.xml")
            .addAsResource("META-INF/create.sql")
            .addAsResource("META-INF/drop.sql")
            .addAsResource("META-INF/load.sql");
        System.out.println(war.toString(true));
        return war;
    }

    @Test
    public void testEntityGraphMovieDefault() throws Exception {
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
        List<Movie> listMoviesDefaultFetch = movieBean.listMovies();
        for (Movie movie : listMoviesDefaultFetch) {
            assertFalse(persistenceUnitUtil.isLoaded(movie, "movieActors"));
            assertTrue(persistenceUnitUtil.isLoaded(movie, "movieDirectors"));
            assertFalse(persistenceUnitUtil.isLoaded(movie, "movieAwards"));
        }
    }

    @Test
    public void testEntityGraphMovieWithActors() throws Exception {
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
        List<Movie> listMoviesWithActorsFetch = movieBean.listMovies("javax.persistence.fetchgraph", "movieWithActors");
        for (Movie movie : listMoviesWithActorsFetch) {
            assertTrue(persistenceUnitUtil.isLoaded(movie, "movieActors"));
            assertFalse(movie.getMovieActors().isEmpty());
            for (MovieActor movieActor : movie.getMovieActors()) {
                assertFalse(persistenceUnitUtil.isLoaded(movieActor, "movieActorAwards"));
            }

            // https://hibernate.atlassian.net/browse/HHH-8776
            // The specification states that by using fetchgraph, attributes should stay unloaded even if defined as
            // EAGER (movieDirectors), but specification also states that the persistence provider is allowed to fetch
            // additional state.
            assertTrue(persistenceUnitUtil.isLoaded(movie, "movieDirectors") ||
                !persistenceUnitUtil.isLoaded(movie, "movieDirectors"));
            assertFalse(persistenceUnitUtil.isLoaded(movie, "movieAwards"));
        }

        List<Movie> listMoviesWithActorsLoad = movieBean.listMovies("javax.persistence.loadgraph", "movieWithActors");
        for (Movie movie : listMoviesWithActorsLoad) {
            // https://java.net/jira/browse/GLASSFISH-21200
            // Glassfish is not processing "javax.persistence.loadgraph".
            assertTrue(persistenceUnitUtil.isLoaded(movie, "movieActors"));
            assertFalse(movie.getMovieActors().isEmpty());
            for (MovieActor movieActor : movie.getMovieActors()) {
                assertFalse(persistenceUnitUtil.isLoaded(movieActor, "movieActorAwards"));
            }

            assertTrue(persistenceUnitUtil.isLoaded(movie, "movieDirectors"));
            assertFalse(persistenceUnitUtil.isLoaded(movie, "movieAwards"));
        }
    }

    @Test
    public void testEntityGraphMovieWithActorsAndAwards() throws Exception {
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
        List<Movie> listMoviesWithActorsFetch =
            movieBean.listMovies("javax.persistence.fetchgraph", "movieWithActorsAndAwards");
        for (Movie movie : listMoviesWithActorsFetch) {
            assertTrue(persistenceUnitUtil.isLoaded(movie, "movieActors"));
            assertFalse(movie.getMovieActors().isEmpty());
            for (MovieActor movieActor : movie.getMovieActors()) {
                assertTrue(persistenceUnitUtil.isLoaded(movieActor, "movieActorAwards") ||
                    !persistenceUnitUtil.isLoaded(movieActor, "movieActorAwards"));
            }

            // https://hibernate.atlassian.net/browse/HHH-8776
            // The specification states that by using fetchgraph, attributes should stay unloaded even if defined as
            // EAGER (movieDirectors), but specification also states that the persistence provider is allowed to fetch
            // additional state.
            assertTrue(persistenceUnitUtil.isLoaded(movie, "movieDirectors") ||
                !persistenceUnitUtil.isLoaded(movie, "movieDirectors"));
            assertFalse(persistenceUnitUtil.isLoaded(movie, "movieAwards"));
        }

        List<Movie> listMoviesWithActorsLoad =
            movieBean.listMovies("javax.persistence.loadgraph", "movieWithActorsAndAwards");
        for (Movie movie : listMoviesWithActorsLoad) {
            // https://java.net/jira/browse/GLASSFISH-21200
            // Glassfish is not processing "javax.persistence.loadgraph".
            assertTrue(persistenceUnitUtil.isLoaded(movie, "movieActors"));
            assertFalse(movie.getMovieActors().isEmpty());
            for (MovieActor movieActor : movie.getMovieActors()) {
                assertTrue(persistenceUnitUtil.isLoaded(movieActor, "movieActorAwards"));
            }

            assertTrue(persistenceUnitUtil.isLoaded(movie, "movieDirectors"));
            assertFalse(persistenceUnitUtil.isLoaded(movie, "movieAwards"));
        }
    }

    @Test
    public void testEntityGraphProgrammatically() throws Exception {
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        EntityGraph<Movie> fetchAll = entityManager.createEntityGraph(Movie.class);
        fetchAll.addSubgraph(Movie_.movieActors);
        fetchAll.addSubgraph(Movie_.movieDirectors);
        fetchAll.addSubgraph(Movie_.movieAwards);

        List<Movie> moviesFetchAll = movieBean.listMovies("javax.persistence.fetchgraph", fetchAll);
        for (Movie movie : moviesFetchAll) {
            assertTrue(persistenceUnitUtil.isLoaded(movie, "movieActors"));
            assertTrue(persistenceUnitUtil.isLoaded(movie, "movieDirectors"));
            assertTrue(persistenceUnitUtil.isLoaded(movie, "movieAwards"));
        }
    }

    @Test
    public void testEntityGraphWithNamedParameters() throws Exception {
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
        List<Movie> listMovieById = movieBean.listMoviesById(1, "javax.persistence.fetchgraph", "movieWithActors");
        assertFalse(listMovieById.isEmpty());
        assertEquals(1, listMovieById.size());
        for (Movie movie : listMovieById) {
            assertTrue(movie.getId().equals(1));

            assertTrue(persistenceUnitUtil.isLoaded(movie, "movieActors"));
            assertFalse(movie.getMovieActors().isEmpty());
            for (MovieActor movieActor : movie.getMovieActors()) {
                assertFalse(persistenceUnitUtil.isLoaded(movieActor, "movieActorAwards"));
            }

            // https://hibernate.atlassian.net/browse/HHH-8776
            // The specification states that by using fetchgraph, attributes should stay unloaded even if defined as
            // EAGER (movieDirectors), but specification also states that the persistence provider is allowed to fetch
            // additional state.
            assertTrue(persistenceUnitUtil.isLoaded(movie, "movieDirectors") ||
                !persistenceUnitUtil.isLoaded(movie, "movieDirectors"));
            assertFalse(persistenceUnitUtil.isLoaded(movie, "movieAwards"));
        }
    }

    @Test
    public void testEntityGraphWithNamedParametersList() throws Exception {
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
        // Hibernate fails mixing Entity Graphs and Named Parameters with "IN". Throws NPE
        List<Movie> listMoviesByIds =
            movieBean.listMoviesByIds(Arrays.asList(1, 2), "javax.persistence.fetchgraph", "movieWithActors");

        assertFalse(listMoviesByIds.isEmpty());
        assertEquals(2, listMoviesByIds.size());
        for (Movie movie : listMoviesByIds) {
            assertTrue(movie.getId().equals(1) || movie.getId().equals(2));

            assertTrue(persistenceUnitUtil.isLoaded(movie, "movieActors"));
            assertFalse(movie.getMovieActors().isEmpty());
            for (MovieActor movieActor : movie.getMovieActors()) {
                assertFalse(persistenceUnitUtil.isLoaded(movieActor, "movieActorAwards"));
            }

            // https://hibernate.atlassian.net/browse/HHH-8776
            // The specification states that by using fetchgraph, attributes should stay unloaded even if defined as
            // EAGER (movieDirectors), but specification also states that the persistence provider is allowed to fetch
            // additional state.
            assertTrue(persistenceUnitUtil.isLoaded(movie, "movieDirectors") ||
                !persistenceUnitUtil.isLoaded(movie, "movieDirectors"));
            assertFalse(persistenceUnitUtil.isLoaded(movie, "movieAwards"));
        }
    }
}
