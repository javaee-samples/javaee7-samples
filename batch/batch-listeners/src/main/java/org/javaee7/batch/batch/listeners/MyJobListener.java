package org.javaee7.batch.batch.listeners;

import javax.batch.api.listener.AbstractJobListener;
import javax.inject.Named;

/**
 * @author Arun Gupta
 */
@Named
public class MyJobListener extends AbstractJobListener {

    @Override
    public void beforeJob() {
        BatchListenerRecorder.batchListenersCountDownLatch.countDown();
        System.out.println("MyJobListener.beforeJob");
    }

    @Override
    public void afterJob() {
        BatchListenerRecorder.batchListenersCountDownLatch.countDown();
        System.out.println("MyJobListener.afterJob");
    }
}
