package org.javaee7.batch.samples.scheduling;

import static java.util.Calendar.SECOND;
import static java.util.concurrent.TimeUnit.MINUTES;
import static javax.batch.runtime.BatchStatus.COMPLETED;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.batch.runtime.BatchRuntime;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.LastExecution;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.concurrent.Trigger;

/**
 * @author arungupta
 */
@Stateless
@Local(MyManagedScheduledBatch.class)
public class MyManagedScheduledBatchBean implements MyManagedScheduledBatch {
    
    @Resource
    private ManagedScheduledExecutorService executor;

    @Override
    public void runJob() {
        executor.schedule(createJob(), new Trigger() {

            @Override
            public Date getNextRunTime(LastExecution lastExecutionInfo, Date taskScheduledTime) {
                if (MyJob.executedBatchs.size() >= 3) {
                    return null;
                }

                Calendar cal = Calendar.getInstance();

                if (lastExecutionInfo == null) {
                    cal.setTime(taskScheduledTime);
                } else {
                    cal.setTime(lastExecutionInfo.getRunStart());
                }

                cal.add(SECOND, 10);
                return cal.getTime();
            }

            @Override
            public boolean skipRun(LastExecution lastExecutionInfo, Date scheduledRunTime) {
                List<Long> executedBatchs = MyJob.executedBatchs;

                for (Long executedBatch : executedBatchs) {
                    if (!BatchRuntime.getJobOperator().getJobExecution(executedBatch).getBatchStatus().equals(
                        COMPLETED)) {
                        return true;
                    }
                }

                return false;
            }

        });
    }

    public void runJob2() {
        executor.scheduleWithFixedDelay(new MyJob(), 1, 2, MINUTES);
    }

    protected MyJob createJob() {
        return new MyJob();
    }
}
