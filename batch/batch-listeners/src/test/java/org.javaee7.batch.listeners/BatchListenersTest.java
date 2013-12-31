package org.javaee7.batch.listeners;

import org.javaee7.util.BatchTestHelper;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Roberto Cortez
 */
@RunWith(Arquillian.class)
public class BatchListenersTest {
    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class)
                .addClass(BatchTestHelper.class)
                .addPackage("org.javaee7.batch.listeners")
                .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
                .addAsResource("META-INF/batch-jobs/myJob.xml");
        System.out.println(war.toString(true));
        return war;
    }

    @Test
    public void testBatchListeners() throws Exception {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Long executionId = jobOperator.start("myJob", new Properties());
        JobExecution jobExecution = jobOperator.getJobExecution(executionId);

        BatchTestHelper.keepTestAlive(jobExecution);

        List<StepExecution> stepExecutions = jobOperator.getStepExecutions(executionId);
        for (StepExecution stepExecution : stepExecutions) {
            if (stepExecution.getStepName().equals("myStep")) {
                Map<Metric.MetricType, Long> metricsMap = BatchTestHelper.getMetricsMap(stepExecution.getMetrics());

                assertEquals(10L, metricsMap.get(Metric.MetricType.READ_COUNT).longValue());
                assertEquals(10L / 2L, metricsMap.get(Metric.MetricType.WRITE_COUNT).longValue());
                assertEquals(10L / 3 + (10L % 3 > 0 ? 1 : 0), metricsMap.get(Metric.MetricType.COMMIT_COUNT).longValue());
            }
        }

        assertTrue(BatchListenerRecorder.batchListenersCountDownLatch.await(0, TimeUnit.SECONDS));
        assertEquals(jobExecution.getBatchStatus(), BatchStatus.COMPLETED);
    }
}
