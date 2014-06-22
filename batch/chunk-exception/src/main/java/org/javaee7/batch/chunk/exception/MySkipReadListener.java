package org.javaee7.batch.chunk.exception;

import javax.batch.api.chunk.listener.SkipReadListener;
import javax.inject.Named;

/**
 * @author Arun Gupta
 */
@Named
public class MySkipReadListener implements SkipReadListener {
    @Override
    public void onSkipReadItem(Exception e) throws Exception {
        ChunkExceptionRecorder.chunkExceptionsCountDownLatch.countDown();
        System.out.println("MySkipReadListener.onSkipReadItem: " + e.getMessage());
    }
}
