package org.javaee7.batch.batch.listeners;

import java.util.concurrent.CountDownLatch;

/**
 * @author Roberto Cortez
 */
public class BatchListenerRecorder {
    public static CountDownLatch batchListenersCountDownLatch = new CountDownLatch(60);
}
