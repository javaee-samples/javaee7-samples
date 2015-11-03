package org.javaee7.batch.chunk.optional.processor;

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

import static org.junit.Assert.assertEquals;

/**
 * The Batch specification provides a Chunk Oriented processing style. This style is defined by enclosing into a
 * transaction a set of reads, process and write operations via +javax.batch.api.chunk.ItemReader+,
 * +javax.batch.api.chunk.ItemProcessor+ and +javax.batch.api.chunk.ItemWriter+. Items are read one at a time, processed
 * and aggregated. The transaction is then committed when the defined +checkpoint-policy+ is triggered.
 *
 * include::myJob.xml[]
 *
 * A very simple job is defined in the +myJob.xml+ file. The processor is optional, so this is just a single step with a
 * reader and a writer.
 *
 * @author Roberto Cortez
 */
@RunWith(Arquillian.class)
public class BatchChunkOptionalProcessorTest {
    /**
     * We're just going to deploy the application as a +web archive+. Note the inclusion of the following files:
     *
     * [source,file]
     * ----
     * /META-INF/batch-jobs/myJob.xml
     * ----
     *
     * The +myJob.xml+ file is needed for running the batch definition.
     */
    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class)
            .addClass(BatchTestHelper.class)
            .addPackage("org.javaee7.batch.chunk.optional.processor")
            .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
            .addAsResource("META-INF/batch-jobs/myJob.xml");
        System.out.println(war.toString(true));
        return war;
    }

    /**
     * In the test, we're just going to invoke the batch execution and wait for completion. To validate the test
     * expected behaviour we need to query the +javax.batch.runtime.Metric+ object available in the step execution.
     *
     * The batch process itself will read 10 elements from numbers  1 to 10, and write the same elements. Commits are
     * executed after 3 elements are read.
     *
     * @throws Exception an exception if the batch could not complete successfully.
     */
    @Test
    public void testBatchChunkOptionalProcessor() throws Exception {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Long executionId = jobOperator.start("myJob", new Properties());
        JobExecution jobExecution = jobOperator.getJobExecution(executionId);

        jobExecution = BatchTestHelper.keepTestAlive(jobExecution);

        List<StepExecution> stepExecutions = jobOperator.getStepExecutions(executionId);
        for (StepExecution stepExecution : stepExecutions) {
            if (stepExecution.getStepName().equals("myStep")) {
                Map<Metric.MetricType, Long> metricsMap = BatchTestHelper.getMetricsMap(stepExecution.getMetrics());

                // <1> The read count should be 10 elements. Check +MyItemReader+.
                assertEquals(10L, metricsMap.get(Metric.MetricType.READ_COUNT).longValue());
                // <2> The write count should be same 10 read elements.
                assertEquals(10L, metricsMap.get(Metric.MetricType.WRITE_COUNT).longValue());
                // <3> The commit count should be 4. Checkpoint is on every 3rd read, 4 commits for read elements.
                assertEquals(10L / 3 + (10L % 3 > 0 ? 1 : 0),
                    metricsMap.get(Metric.MetricType.COMMIT_COUNT).longValue());
            }
        }

        // <4> Job should be completed.
        assertEquals(BatchStatus.COMPLETED, jobExecution.getBatchStatus());
    }
}
