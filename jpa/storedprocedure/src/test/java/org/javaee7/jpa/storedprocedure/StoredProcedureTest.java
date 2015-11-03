package org.javaee7.jpa.storedprocedure;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.assertFalse;

/**
 * @author Roberto Cortez
 */
@RunWith(Arquillian.class)
public class StoredProcedureTest {
    @Inject
    private MovieBean movieBean;

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class)
            .addPackage("org.javaee7.jpa.storedprocedure")
            .addAsResource("META-INF/persistence.xml")
            .addAsResource("META-INF/create.sql")
            .addAsResource("META-INF/drop.sql")
            .addAsResource("META-INF/load.sql");
        System.out.println(war.toString(true));
        return war;
    }

    @Test
    public void testStoredProcedure() throws Exception {
        List<Movie> movies = movieBean.listMovies();
        assertFalse(movies.isEmpty());

        //movieBean.executeStoredProcedure();
    }
}
