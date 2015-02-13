package org.javaee7.batch.samples.scheduling;

import javax.batch.runtime.BatchRuntime;
import javax.ejb.Schedule;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Roberto Cortez
 */
public abstract class AbstractTimerBatch {
    public static List<Long> executedBatchs = new ArrayList<>();

    @Schedule(hour = "*", minute = "0", second = "0")
    public void myJob() {
        executedBatchs.add(BatchRuntime.getJobOperator().start("myJob", new Properties()));
        afterRun();
    }

    protected void afterRun() {
    }
}
