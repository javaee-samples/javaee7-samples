package org.javaee7.concurrency.managedscheduledexecutor;

import javax.enterprise.concurrent.LastExecution;
import javax.enterprise.concurrent.Trigger;
import java.util.Date;

/**
 * @author Arun Gupta
 */
public class MyTrigger implements Trigger {

    private final Date firetime;

    public MyTrigger(Date firetime) {
        this.firetime = firetime;
    }

    @Override
    public Date getNextRunTime(LastExecution le, Date taskScheduledTime) {
        if (firetime.before(taskScheduledTime)) {
            return null;
        }
        return firetime;
    }

    @Override
    public boolean skipRun(LastExecution le, Date scheduledRunTime) {
        return firetime.before(scheduledRunTime);
    }
    
}
