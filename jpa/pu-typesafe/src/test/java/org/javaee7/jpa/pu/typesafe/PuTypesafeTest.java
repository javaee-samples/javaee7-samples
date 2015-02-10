package org.javaee7.jpa.pu.typesafe;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Roberto Cortez
 */
@RunWith(Arquillian.class)
public class PuTypesafeTest {
    @Inject
    private MySessionBean bean;

    @Inject
    @DefaultDatabase
    private EntityManager defaultEM;

    @PersistenceContext(name = "defaultPU")
    private EntityManager persistenceContextEM;

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class)
            .addPackage("org.javaee7.jpa.pu.typesafe")
            .addAsResource("META-INF/persistence.xml")
            .addAsResource("META-INF/create.sql")
            .addAsResource("META-INF/drop.sql")
            .addAsResource("META-INF/load.sql");
        System.out.println(war.toString(true));
        return war;
    }

    @Test
    public void testPuTypesafe() throws Exception {
        List<Movie> movies = bean.listMovies();
        assertFalse(movies.isEmpty());

        assertNotNull(defaultEM);

        List<Movie> defaultFindAll = defaultEM.createNamedQuery("Movie.findAll", Movie.class).getResultList();
        assertFalse(defaultFindAll.isEmpty());

        assertArrayEquals(movies.toArray(), defaultFindAll.toArray());

        List<Movie> persistenceContextFindAll =
            persistenceContextEM.createNamedQuery("Movie.findAll", Movie.class).getResultList();

        assertArrayEquals(movies.toArray(), persistenceContextFindAll.toArray());
    }
}
