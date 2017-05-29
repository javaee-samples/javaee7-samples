package org.javaee7.batch.samples.scheduling;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.batch.runtime.BatchRuntime;
import javax.ejb.Schedule;

/**
 * @author Roberto Cortez
 */
public abstract class AbstractTimerBatch {
    
    public static List<Long> executedBatchs = new ArrayList<>();

    @Schedule(hour = "*", minute = "0", second = "0", persistent = false)
    public void myJob() {
        executedBatchs.add(BatchRuntime.getJobOperator().start("myJob", new Properties()));
        afterRun();
    }

    protected void afterRun() {
    }
}
