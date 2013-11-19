package org.javaee7.batch.util;

import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.Metric;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Roberto Cortez
 */
public final class BatchTestHelper {
    private static final int MAX_TRIES = 10;
    private static final int THREAD_SLEEP = 100;

    private BatchTestHelper() {
        throw new UnsupportedOperationException();
    }

    /**
     * We need to keep the test running because JobOperator runs the batch job in an asynchronous way, so the
     * JobExecution can be properly updated with the running job status.
     *
     * @param jobExecution
     *         the JobExecution of the job that is being runned on JobOperator.
     *
     * @throws InterruptedException thrown by Thread.sleep.
     */
    public static void keepTestAlive(JobExecution jobExecution) throws InterruptedException {
        int maxTries = 0;
        while (!jobExecution.getBatchStatus().equals(BatchStatus.COMPLETED)) {
            if (maxTries < MAX_TRIES) {
                maxTries++;
                Thread.sleep(THREAD_SLEEP);
            } else {
                break;
            }
        }
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
