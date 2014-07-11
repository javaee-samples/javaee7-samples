package org.javaee7.batch.samples.scheduling;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.concurrent.CountDownLatch;

/**
 * @author Roberto Cortez
 */
@Startup
@Singleton
public class MyTimerScheduleAlternative extends AbstractTimerBatch {
    public static CountDownLatch timerScheduleCountDownLatch = new CountDownLatch(3);

    @Override
    @Schedule(hour = "*", minute = "*", second = "*/15")
    public void myJob() {
        super.myJob();
    }

    @Override
    protected void afterRun() {
        timerScheduleCountDownLatch.countDown();
    }
}
