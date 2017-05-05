package org.javaee7.jpa.locking.optimistic;

import static java.util.concurrent.TimeUnit.SECONDS;
import static javax.ejb.TransactionAttributeType.REQUIRED;

import java.util.concurrent.CountDownLatch;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.enterprise.inject.Alternative;

/**
 * @author Roberto Cortez
 */
@Alternative
@Stateless
public class MovieBeanAlternative extends MovieBean {
    
    public static CountDownLatch lockCountDownLatch = new CountDownLatch(1);
    public static CountDownLatch readCountDownLatch = new CountDownLatch(1);

    @Override
    @TransactionAttribute(REQUIRED)
    public Movie readMovie(Integer id) {
        System.out.println("MovieBeanAlternative.readMovie");
        Movie movie = super.readMovie(id);
        try {
            readCountDownLatch.await(10, SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return movie;
    }

    @Override
    @TransactionAttribute(REQUIRED)
    public void updateMovie(Integer id, String name) {
        System.out.println("MovieBeanAlternative.updateMovie");
        super.updateMovie(id, name);
        try {
            System.out.println("MovieBeanAlternative.updateMovie waiting for lockCountDownLatch");
            lockCountDownLatch.await(10, SECONDS);
            System.out.println("MovieBeanAlternative.updateMovie done waiting for lockCountDownLatch");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    @TransactionAttribute(REQUIRED)
    public void updateMovie2(Integer id, String name) {
        System.out.println("MovieBeanAlternative.updateMovie2");
        
        System.out.println("MovieBeanAlternative.updateMovie2 Reading entity");
        // We're reading the movie first, with the attention for this movie to become "stale"
        Movie movie = super.readMovie(id);
        
        try {
            // Now wait for the movie to be deleted by the other thread
            System.out.println("MovieBeanAlternative.updateMovie2 waiting for lockCountDownLatch");
            boolean gotLock = lockCountDownLatch.await(10, SECONDS);
            System.out.println("MovieBeanAlternative.updateMovie2 done waiting for lockCountDownLatch. Got lock:" + gotLock);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // If we got the lock from the lockCountDownLatch, then it means the movie has been deleted. 
        // If we try to update the movie now it has to throw an OptimisticLockException
        // (if we didn't got the lock the test will just fail)
        super.updateMovie(movie, name);
    }
    
    @Override
    @TransactionAttribute(REQUIRED)
    public void deleteMovie(Integer id) {
        System.out.println("MovieBeanAlternative.deleteMovie");
        super.deleteMovie(id);
    }
}
