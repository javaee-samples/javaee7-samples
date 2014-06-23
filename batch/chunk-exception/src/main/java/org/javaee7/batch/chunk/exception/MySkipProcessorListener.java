package org.javaee7.batch.chunk.exception;

import javax.batch.api.chunk.listener.SkipProcessListener;
import javax.inject.Named;

/**
 * @author Arun Gupta
 */
@Named
public class MySkipProcessorListener implements SkipProcessListener {
    @Override
    public void onSkipProcessItem(Object t, Exception e) throws Exception {
        ChunkExceptionRecorder.chunkExceptionsCountDownLatch.countDown();
        System.out.println("MySkipProcessorListener.onSkipProcessItem: " + ((MyInputRecord) t).getId() + ", " + e.getMessage());
    }
}
