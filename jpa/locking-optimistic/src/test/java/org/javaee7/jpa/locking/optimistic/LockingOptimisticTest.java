package org.javaee7.jpa.locking.optimistic;

import static java.lang.Thread.sleep;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Roberto Cortez
 */
@RunWith(Arquillian.class)
public class LockingOptimisticTest {
    
    @Inject
    private MovieBean movieBean;

    @Resource
    private ManagedExecutorService executor;

    @Deployment
    public static WebArchive createDeployment() {
        BeansDescriptor beansXml = Descriptors.create(BeansDescriptor.class);

        WebArchive war = ShrinkWrap.create(WebArchive.class)
            .addPackage("org.javaee7.jpa.locking.optimistic")
            .addAsResource("META-INF/persistence.xml")
            .addAsResource("META-INF/load.sql")
            .addAsWebInfResource(
                new StringAsset(beansXml.getOrCreateAlternatives()
                    .clazz(MovieBeanAlternative.class.getName()).up().exportAsString()),
                beansXml.getDescriptorName());
        
        System.out.println(war.toString(true));
        
        return war;
    }

    @Test
    public void testLockingOptimisticUpdateAndRead() throws Exception {
        
        System.out.println("Enter testLockingOptimisticUpdateAndRead");
        
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

        assertTrue(testCountDownLatch.await(10, SECONDS));
        assertEquals("INCEPTION UR", movieBean.findMovie(3).getName());
    }

    @Test
    public void testLockingOptimisticReadAndUpdate() throws Exception {
        
        System.out.println("Enter testLockingOptimisticReadAndUpdate");
        
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
        
        System.out.println("Enter testLockingOptimisticDelete");
        
        resetCountDownLatches();
        List<Movie> movies = movieBean.listMovies();
        assertFalse(movies.isEmpty());

        final CountDownLatch testCountDownLatch1 = new CountDownLatch(1);
        final CountDownLatch testCountDownLatch2 = new CountDownLatch(1);

        executor.execute(new Runnable() {
            @Override
            public void run() {
                
                System.out.println("Update thread " + Thread.currentThread().getId() + " at " + System.nanoTime());
                
                try {
                    testCountDownLatch1.countDown();
                    movieBean.updateMovie2(3, "INCEPTION");
                } catch (RuntimeException e) { // Should throw an javax.persistence.OptimisticLockException? The Exception is wrapped around an javax.ejb.EJBException
                    testCountDownLatch2.countDown();
                }
            }
        });

        executor.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("Delete thread " + Thread.currentThread().getId() + " at " + System.nanoTime());
                try {
                    testCountDownLatch1.await(10, SECONDS);
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                movieBean.deleteMovie(3);
                MovieBeanAlternative.lockCountDownLatch.countDown();
            }
        });

        assertTrue(testCountDownLatch2.await(20, SECONDS));
    }

    private void resetCountDownLatches() {
        MovieBeanAlternative.lockCountDownLatch = new CountDownLatch(1);
        MovieBeanAlternative.readCountDownLatch = new CountDownLatch(1);
    }
}
