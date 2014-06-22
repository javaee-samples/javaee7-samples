package org.javaee7.batch.batch.listeners;

import javax.batch.api.chunk.listener.AbstractChunkListener;
import javax.inject.Named;

/**
 * @author Arun Gupta
 */
@Named
public class MyChunkListener extends AbstractChunkListener {

    @Override
    public void beforeChunk() throws Exception {
        BatchListenerRecorder.batchListenersCountDownLatch.countDown();
        System.out.println("MyChunkListener.beforeChunk");
    }

    @Override
    public void afterChunk() throws Exception {
        BatchListenerRecorder.batchListenersCountDownLatch.countDown();
        System.out.println("MyChunkListener.afterChunk");
    }
}
