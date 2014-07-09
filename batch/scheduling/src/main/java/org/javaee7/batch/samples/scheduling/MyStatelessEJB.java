package org.javaee7.batch.samples.scheduling;

import javax.annotation.Resource;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.LastExecution;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.concurrent.Trigger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author arungupta
 */
@Stateless
public class MyStatelessEJB {
    @Resource
    private ManagedScheduledExecutorService executor;

    public void runJob() {
        executor.schedule(new MyJob(), new Trigger() {

            @Override
            public Date getNextRunTime(LastExecution lastExecutionInfo, Date taskScheduledTime) {
                if (MyJob.managedScheduledCountDownLatch.getCount() == 0) {
                    return null;
                }

                Calendar cal = Calendar.getInstance();

                if (lastExecutionInfo == null) {
                    cal.setTime(taskScheduledTime);
                } else {
                    cal.setTime(lastExecutionInfo.getRunStart());
                }

                cal.add(Calendar.SECOND, 10);
                return cal.getTime();
            }

            @Override
            public boolean skipRun(LastExecution lastExecutionInfo, Date scheduledRunTime) {
                List<Long> executedBatchs = MyJob.executedBatchs;

                for (Long executedBatch : executedBatchs) {
                    if (!BatchRuntime.getJobOperator().getJobExecution(executedBatch).getBatchStatus().equals(
                            BatchStatus.COMPLETED)) {
                        return true;
                    }
                }

                return false;
            }

        });
    }

    public void runJob2() {
        executor.scheduleWithFixedDelay(new MyJob(), 1, 2, TimeUnit.MINUTES);
    }
}
