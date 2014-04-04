package org.javaee7.batch.chunk.exception;

import javax.batch.api.chunk.listener.RetryReadListener;
import javax.inject.Named;

/**
 * @author Roberto Cortez
 */
@Named
public class MyRetryReadListener implements RetryReadListener {
    @Override
    public void onRetryReadException(Exception ex) throws Exception {
        ChunkExceptionRecorder.retryReadExecutions++;
        ChunkExceptionRecorder.chunkExceptionsCountDownLatch.countDown();
        System.out.println("MyRetryReadListener.onRetryReadException " + ex.getMessage());
    }
}
