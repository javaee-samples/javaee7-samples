package org.javaee7.jpa.locking.optimistic;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.inject.Alternative;
import java.util.concurrent.CountDownLatch;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author Roberto Cortez
 */
@Alternative
@Stateless
public class MovieBeanAlternative extends MovieBean {
    public static CountDownLatch lockCountDownLatch = new CountDownLatch(1);
    public static CountDownLatch readCountDownLatch = new CountDownLatch(1);

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
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
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void updateMovie(Integer id, String name) {
        System.out.println("MovieBeanAlternative.updateMovie");
        super.updateMovie(id, name);
        try {
            lockCountDownLatch.await(10, SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
