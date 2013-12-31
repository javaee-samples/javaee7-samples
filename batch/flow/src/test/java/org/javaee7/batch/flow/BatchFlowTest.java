package org.javaee7.batch.flow;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author Roberto Cortez
 */
@RunWith(Arquillian.class)
public class BatchFlowTest {
    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class)
                .addClass(BatchTestHelper.class)
                .addPackage("org.javaee7.batch.flow")
                .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
                .addAsResource("META-INF/batch-jobs/myJob.xml");
        System.out.println(war.toString(true));
        return war;
    }

    @Test
    public void testBatchFlow() throws Exception {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Long executionId = jobOperator.start("myJob", new Properties());
        JobExecution jobExecution = jobOperator.getJobExecution(executionId);

        BatchTestHelper.keepTestAlive(jobExecution);

        List<StepExecution> stepExecutions = jobOperator.getStepExecutions(executionId);
        List<String> executedSteps = new ArrayList<>();
        for (StepExecution stepExecution : stepExecutions) {
            executedSteps.add(stepExecution.getStepName());

            if (stepExecution.getStepName().equals("step2")) {
                Map<Metric.MetricType, Long> metricsMap = BatchTestHelper.getMetricsMap(stepExecution.getMetrics());
                System.out.println(metricsMap);
                assertEquals(5L, metricsMap.get(Metric.MetricType.READ_COUNT).longValue());
                assertEquals(5L, metricsMap.get(Metric.MetricType.WRITE_COUNT).longValue());
                assertEquals(5L / 3 + (5 % 3 > 0 ? 1 : 0), metricsMap.get(Metric.MetricType.COMMIT_COUNT).longValue());
            }
        }

        assertEquals(3, stepExecutions.size());
        assertArrayEquals(new String[]{"step1", "step2", "step3"}, executedSteps.toArray());
        assertEquals(BatchStatus.COMPLETED, jobExecution.getBatchStatus());
    }
}
