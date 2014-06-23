package org.javaee7.batch.batch.listeners;

import javax.batch.api.listener.AbstractStepListener;
import javax.inject.Named;

/**
 * @author Arun Gupta
 */
@Named
public class MyStepListener extends AbstractStepListener {

    @Override
    public void beforeStep() throws Exception {
        BatchListenerRecorder.batchListenersCountDownLatch.countDown();
        System.out.println("MyStepListener.beforeStep");
    }

    @Override
    public void afterStep() throws Exception {
        BatchListenerRecorder.batchListenersCountDownLatch.countDown();
        System.out.println("MyStepListener.afterStep");
    }
}
