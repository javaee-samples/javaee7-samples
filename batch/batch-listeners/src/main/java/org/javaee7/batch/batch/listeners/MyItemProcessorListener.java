package org.javaee7.batch.batch.listeners;

import javax.batch.api.chunk.listener.AbstractItemProcessListener;
import javax.inject.Named;

/**
 * @author Arun Gupta
 */
@Named
public class MyItemProcessorListener extends AbstractItemProcessListener {

    @Override
    public void beforeProcess(Object item) throws Exception {
        BatchListenerRecorder.batchListenersCountDownLatch.countDown();
        System.out.println("MyItemProcessorListener.beforeProcess: " + item);
    }

    @Override
    public void afterProcess(Object item, Object result) throws Exception {
        BatchListenerRecorder.batchListenersCountDownLatch.countDown();
        System.out.println("MyItemProcessorListener.afterProcess: " + item + ", " + result);
    }

    @Override
    public void onProcessError(Object item, Exception ex) throws Exception {
        BatchListenerRecorder.batchListenersCountDownLatch.countDown();
        System.out.println("MyItemProcessorListener.onProcessError: " + item + ", " + ex.getLocalizedMessage());
    }
}
