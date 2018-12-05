package org.javaee7.util;

import static javax.batch.runtime.BatchStatus.COMPLETED;

import java.util.HashMap;
import java.util.Map;

import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.Metric;

/**
 * @author Roberto Cortez
 */
public final class BatchTestHelper {
    private static final int MAX_TRIES = 40;
    private static final int THREAD_SLEEP = 1000;

    private BatchTestHelper() {
        throw new UnsupportedOperationException();
    }

    /**
     * We need to keep the test running because JobOperator runs the batch job in an asynchronous way.
     * Returns when either the job execution completes or we have polled the maximum number of tries.
     *
     * @param jobExecution
     *         the JobExecution of the job that is being runned on JobOperator.
     * @return the most recent JobExecution obtained for this execution
     * @throws InterruptedException thrown by Thread.sleep.
     */
    public static JobExecution keepTestAlive(JobExecution jobExecution) throws InterruptedException {
        System.out.println(" * Entering keepTestAlive, completed is: " + jobExecution.getBatchStatus().equals(COMPLETED));
        
        int maxTries = 0;
        while (!jobExecution.getBatchStatus().equals(COMPLETED)) {
            if (maxTries < MAX_TRIES) {
                maxTries++;
                Thread.sleep(THREAD_SLEEP);
                jobExecution = BatchRuntime.getJobOperator().getJobExecution(jobExecution.getExecutionId());
            } else {
                break;
            }
        }
        Thread.sleep(THREAD_SLEEP);
        
        System.out.println(" * Exiting keepTestAlive, completed is: " + jobExecution.getBatchStatus().equals(COMPLETED));
        
        return jobExecution;
    }

    /**
     * Convert the Metric array contained in StepExecution to a key-value map for easy access to Metric parameters.
     *
     * @param metrics
     *         a Metric array contained in StepExecution.
     *
     * @return a map view of the metrics array.
     */
    public static Map<Metric.MetricType, Long> getMetricsMap(Metric[] metrics) {
        Map<Metric.MetricType, Long> metricsMap = new HashMap<>();
        for (Metric metric : metrics) {
            metricsMap.put(metric.getType(), metric.getValue());
        }
        return metricsMap;
    }
}
