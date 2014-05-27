package org.javaee7.batch.chunk.exception;

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
 * In this sample we're going to process a few record and mix some exceptions during read, processing and write of the
 * chunk. Exceptions are a natural part of batch processing, and the batch itself should be prepared to deal with
 * exceptions during processing. These exceptions are configured in the job xml file (+myjob.xml+).
 *
 * Batch processing deals with two kinds of exceptions: skippable and retryable. Skippable Exceptions are used to skip
 * elements during reading, processing and writing and continue to the next element. Retryable Exceptions on the other
 * hand when thrown will try to retry the chunk on which the exceptiong occurred.
 *
 * When the same exception is specified as both retryable and skippable, retryable takes precedence over skippable during
 * regular processing of the chunk. While the chunk is retrying, skippable takes precedence over retryable since the exception
 * is already being retried.
 *
 * The Reader:
 * include::MyItemReader[]
 *
 * Just reads elements from a list and simulate a retry exception.
 *
 * The Processor:
 * include::MyItemProcessor[]
 *
 * Process and simulate a skip exception.
 *
 * The Writer:
 * include::MyItemWriter[]
 *
 * The writer will retry an exception and then skip it.
 *
 * The batch specification also allows you to provide listeners for skipping and retrying for every operation. Have a
 * look into the following classes:
 *
 * * +MySkipReadListener+
 * * +MySkipProcessorListener+
 * * +MySkipWriteListener+
 * * +MyRetryReadListener+
 * * +MyRetryProcessorListener+
 * * +MyRetryWriteListener+
 *
 * @author Roberto Cortez
 */
@RunWith(Arquillian.class)
public class BatchChunkExceptionTest {
    /**
     * We're just going to deploy the application as a +web archive+. Note the inclusion of the following files:
     *
     * [source,file]
     * ----
     * /META-INF/batch-jobs/myjob.xml
     * ----
     *
     * The +myjob.xml+ file is needed for running the batch definition.
     */
    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class)
                .addClass(BatchTestHelper.class)
                .addPackage("org.javaee7.batch.chunk.exception")
                .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
                .addAsResource("META-INF/batch-jobs/myJob.xml");
        System.out.println(war.toString(true));
        return war;
    }

    /**
     * In the test, we're just going to invoke the batch execution and wait for completion. To validate the test
     * expected behaviour we need to query the +Metric[]+ object available in the step execution.
     *
     * @throws Exception an exception if the batch could not complete successfully.
     */
    @Test
    public void testBatchChunkException() throws Exception {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Long executionId = jobOperator.start("myJob", new Properties());
        JobExecution jobExecution = jobOperator.getJobExecution(executionId);

        BatchTestHelper.keepTestAlive(jobExecution);

        List<StepExecution> stepExecutions = jobOperator.getStepExecutions(executionId);
        for (StepExecution stepExecution : stepExecutions) {
            if (stepExecution.getStepName().equals("myStep")) {
                Map<Metric.MetricType, Long> metricsMap = BatchTestHelper.getMetricsMap(stepExecution.getMetrics());

                assertEquals(1L, metricsMap.get(Metric.MetricType.PROCESS_SKIP_COUNT).longValue());
                // There are a few differences between Glassfish and Wildfly. Needs investigation.
                //assertEquals(1L, metricsMap.get(Metric.MetricType.WRITE_SKIP_COUNT).longValue());
                assertEquals(1L, ChunkExceptionRecorder.retryReadExecutions);
            }
        }

        assertTrue(ChunkExceptionRecorder.chunkExceptionsCountDownLatch.await(0, TimeUnit.SECONDS));
        assertEquals(BatchStatus.COMPLETED, jobExecution.getBatchStatus());
    }
}
