package org.samples.wildfly.scheduling.batch;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.LastExecution;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.concurrent.Trigger;

/**
 * @author arungupta
 */
@Stateless
public class MyStatelessEJB {
    @Resource
    ManagedScheduledExecutorService executor;
    
    public void runJob() {
        executor.schedule(new MyJob(), new Trigger() {

            @Override
            public Date getNextRunTime(LastExecution lastExecutionInfo, Date taskScheduledTime) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(taskScheduledTime);
                cal.add(Calendar.DATE, 1);
                return cal.getTime();
            }

            @Override
            public boolean skipRun(LastExecution lastExecutionInfo, Date scheduledRunTime) {
                return null == lastExecutionInfo;
            }
            
        });
    }
    
    public void cancelJob() {
        executor.shutdown();
    }
    
    public void runJob2() {
        executor.scheduleWithFixedDelay(new MyJob(), 1, 2, TimeUnit.MINUTES);
    }
}
