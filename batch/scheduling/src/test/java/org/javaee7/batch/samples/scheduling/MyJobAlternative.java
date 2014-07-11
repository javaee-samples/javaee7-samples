package org.javaee7.batch.samples.scheduling;

import java.util.concurrent.CountDownLatch;

/**
 * @author Roberto Cortez
 */
public class MyJobAlternative extends MyJob {
    public static CountDownLatch managedScheduledCountDownLatch = new CountDownLatch(3);

    @Override
    protected void afterRun() {
        managedScheduledCountDownLatch.countDown();
    }
}
