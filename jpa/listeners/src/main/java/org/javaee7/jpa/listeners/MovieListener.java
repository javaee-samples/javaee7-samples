package org.javaee7.jpa.listeners;

import javax.persistence.*;
import java.util.concurrent.CountDownLatch;

/**
 * @author Arun Gupta
 */
public class MovieListener {
    public static CountDownLatch entityListenersCountDownLatch = new CountDownLatch(26);

    public static boolean postLoadInvoked;
    public static boolean prePersistInvoked;
    public static boolean postPersistInvoked;
    public static boolean preUpdateInvoked;
    public static boolean postUpdateInvoked;
    public static boolean preRemoveInvoked;
    public static boolean postRemoveInvoked;

    @PostLoad
    public void newMovieLoad(Movie movie) {
        postLoadInvoked = true;
        entityListenersCountDownLatch.countDown();
        System.out.println("## Movie loaded: " + movie.getName());
    }

    @PrePersist
    public void newMovieAlertBefore(Movie movie) {
        prePersistInvoked = true;
        entityListenersCountDownLatch.countDown();
        System.out.println("## Ready to create new movie: " + movie.getName());
    }

    @PostPersist
    public void newMovieAlertAfter(Movie movie) {
        postPersistInvoked = true;
        entityListenersCountDownLatch.countDown();
        System.out.println("## New movie created: " + movie.getName());
    }

    @PreUpdate
    public void updateMovieAlertBefore(Movie movie) {
        preUpdateInvoked = true;
        entityListenersCountDownLatch.countDown();
        System.out.println("## Ready to update movie: " + movie.getName());
    }

    @PostUpdate
    public void updateMovieAlertAfter(Movie movie) {
        postUpdateInvoked = true;
        entityListenersCountDownLatch.countDown();
        System.out.println("## Movie updated: " + movie.getName());
    }

    @PreRemove
    public void deleteMovieAlertBefore(Movie movie) {
        preRemoveInvoked = true;
        entityListenersCountDownLatch.countDown();
        System.out.println("## Ready to delete movie: " + movie.getName());
    }

    @PostRemove
    public void deleteMovieAlertAfter(Movie movie) {
        postRemoveInvoked = true;
        entityListenersCountDownLatch.countDown();
        System.out.println("## Movie deleted: " + movie.getName());
    }
}
