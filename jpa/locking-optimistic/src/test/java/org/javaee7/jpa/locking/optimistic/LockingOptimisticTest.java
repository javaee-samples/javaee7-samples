package org.javaee7.jpa.locking.optimistic;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * @author Roberto Cortez
 */
@RunWith(Arquillian.class)
public class LockingOptimisticTest {
    @Inject
    private MovieBean movieBean;

    @Resource
    private ManagedScheduledExecutorService executor;

    @Deployment
    public static WebArchive createDeployment() {
        BeansDescriptor beansXml = Descriptors.create(BeansDescriptor.class);

        WebArchive war = ShrinkWrap.create(WebArchive.class)
            .addPackage("org.javaee7.jpa.locking.optimistic")
            .addAsResource("META-INF/persistence.xml")
            .addAsResource("META-INF/load.sql")
            .addAsWebInfResource(
                new StringAsset(beansXml.createAlternatives()
                    .clazz(MovieBeanAlternative.class.getName()).up().exportAsString()),
                beansXml.getDescriptorName());
        System.out.println(war.toString(true));
        return war;
    }

    @Test
    public void testLockingOptimisticUpdateAndRead() throws Exception {
        resetCountDownLatches();
        List<Movie> movies = movieBean.listMovies();
        assertFalse(movies.isEmpty());

        final CountDownLatch testCountDownLatch = new CountDownLatch(1);

        executor.execute(new Runnable() {
            @Override
            public void run() {
                movieBean.updateMovie(3, "INCEPTION UR");
                testCountDownLatch.countDown();
            }
        });

        executor.execute(new Runnable() {
            @Override
            public void run() {
                movieBean.findMovie(3);
                MovieBeanAlternative.lockCountDownLatch.countDown();
            }
        });

        assertTrue(testCountDownLatch.await(10, TimeUnit.SECONDS));
        assertEquals("INCEPTION UR", movieBean.findMovie(3).getName());
    }

    @Test
    public void testLockingOptimisticReadAndUpdate() throws Exception {
        resetCountDownLatches();
        List<Movie> movies = movieBean.listMovies();
        assertFalse(movies.isEmpty());

        final CountDownLatch testCountDownLatch = new CountDownLatch(1);

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    movieBean.readMovie(3);
                } catch (RuntimeException e) { // Should throw an javax.persistence.OptimisticLockException? Hibernate is throwing org.hibernate.OptimisticLockException. Investigate!
                    testCountDownLatch.countDown();
                }
            }
        });

        executor.execute(new Runnable() {
            @Override
            public void run() {
                MovieBeanAlternative.lockCountDownLatch.countDown();
                movieBean.updateMovie(3, "INCEPTION RU");
                MovieBeanAlternative.readCountDownLatch.countDown();
            }
        });

        assertTrue(testCountDownLatch.await(10, TimeUnit.SECONDS));
        assertEquals("INCEPTION RU", movieBean.findMovie(3).getName());
    }

    @Test
    public void testLockingOptimisticDelete() throws Exception {
        resetCountDownLatches();
        List<Movie> movies = movieBean.listMovies();
        assertFalse(movies.isEmpty());

        final CountDownLatch testCountDownLatch = new CountDownLatch(1);

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    movieBean.updateMovie(3, "INCEPTION");
                } catch (RuntimeException e) { // Should throw an javax.persistence.OptimisticLockException? The Exception is wrapped around an javax.ejb.EJBException
                    testCountDownLatch.countDown();
                }
            }
        });

        executor.execute(new Runnable() {
            @Override
            public void run() {
                movieBean.deleteMovie(3);
                MovieBeanAlternative.lockCountDownLatch.countDown();
            }
        });

        assertTrue(testCountDownLatch.await(10, TimeUnit.SECONDS));
    }

    private void resetCountDownLatches() {
        MovieBeanAlternative.lockCountDownLatch = new CountDownLatch(1);
        MovieBeanAlternative.readCountDownLatch = new CountDownLatch(1);
    }
}
