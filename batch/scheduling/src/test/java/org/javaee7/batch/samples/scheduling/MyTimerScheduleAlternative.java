package org.javaee7.batch.samples.scheduling;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * @author Roberto Cortez
 */
@Startup
@Singleton
public class MyTimerScheduleAlternative extends AbstractTimerBatch {
    
    @Override
    @Schedule(hour = "*", minute = "*", second = "*/10", persistent = false)
    public void myJob() {
        super.myJob();
    }

    @Override
    protected void afterRun() {
    }
}
