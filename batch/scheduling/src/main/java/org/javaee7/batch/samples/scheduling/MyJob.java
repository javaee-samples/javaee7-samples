package org.javaee7.batch.samples.scheduling;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import javax.batch.runtime.BatchRuntime;

/**
 * @author arungupta
 */
public class MyJob implements Runnable {
    public static CountDownLatch managedScheduledCountDownLatch = new CountDownLatch(3);
    public static List<Long> executedBatchs = new ArrayList<>();

    public void run() {
        executedBatchs.add(BatchRuntime.getJobOperator().start("myJob", new Properties()));
        managedScheduledCountDownLatch.countDown();
    }
}
