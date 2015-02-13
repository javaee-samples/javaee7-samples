package org.javaee7.batch.samples.scheduling;

import javax.batch.runtime.BatchRuntime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author arungupta
 */
public class MyJob implements Runnable {
    public static List<Long> executedBatchs = new ArrayList<>();

    public void run() {
        executedBatchs.add(BatchRuntime.getJobOperator().start("myJob", new Properties()));
        afterRun();
    }

    protected void afterRun() {
    }
}
