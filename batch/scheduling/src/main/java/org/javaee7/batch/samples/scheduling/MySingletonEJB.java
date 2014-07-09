package org.javaee7.batch.samples.scheduling;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import javax.batch.runtime.BatchRuntime;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * @author arungupta
 */
@Startup
@Singleton
public class MySingletonEJB {
    public static CountDownLatch timerScheduleCountDownLatch = new CountDownLatch(3);
    public static List<Long> executedBatchs = new ArrayList<>();

    @Schedule(hour = "*", minute = "*", second = "*/15")
    public void myJob() {
        executedBatchs.add(BatchRuntime.getJobOperator().start("myJob", new Properties()));
        timerScheduleCountDownLatch.countDown();
    }
}
