package org.javaee7.batch.chunk.exception;

import java.util.concurrent.CountDownLatch;

/**
 * @author Roberto Cortez
 */
public class ChunkExceptionRecorder {
    public static CountDownLatch chunkExceptionsCountDownLatch = new CountDownLatch(3);
    public static int retryReadExecutions = 0;
}
