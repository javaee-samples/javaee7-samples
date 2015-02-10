package org.javaee7.batch.batch.listeners;

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
 * The Batch specification, provides several listeners to notify about specific event occurring during the batch
 * processing execution.
 *
 * Events can be caught via extending the following classes, for the appropriate batch lifecycle event:
 *
 * * +javax.batch.api.listener.AbstractJobListener+
 * * +javax.batch.api.listener.AbstractStepListener+
 * * +javax.batch.api.chunk.listener.AbstractChunkListener+
 * * +javax.batch.api.chunk.listener.AbstractItemReadListener+
 * * +javax.batch.api.chunk.listener.AbstractItemProcessListener+
 * * +javax.batch.api.chunk.listener.AbstractItemWriteListener+
 *
 * The Job Listener:
 * include::MyJobListener[]
 *
 * Allows you to execute code before and after the job execution. Useful to setup and clear resources needed by the job.
 *
 * The Step Listener:
 * include::MyStepListener[]
 *
 * Allows you to execute code before and after the step execution. Useful to setup and clear resources needed by the
 * step.
 *
 * The Chunk Listener:
 * include::MyChunkListener[]
 *
 * Allows you to execute code before and after the chunk processing. Useful to setup and clear resources needed by the
 * chunk.
 *
 * The Read Listener:
 * include::MyItemReadListener[]
 *
 * Allows you to execute code before and after reading a element as well if an error occurs reading that element. Useful
 * to setup additional resources and add additional information to the object reading. You can also provide some logic
 * to treat a failed object read.
 *
 * The Processor Listener:
 * include::MyItemProcessorListener[]
 *
 * Allows you to execute code before and after processing a element as well if an error occurs processing that element.
 * Useful to setup additional resources and add additional information to the object processing. You can also provide
 * some logic to  treat a failed object processing.
 *
 * The Writer Listener:
 * include::MyItemWriteListener[]
 *
 * Allows you to execute code before and after writing a element as well if an error occurs writing that element.
 * Useful to setup additional resources and add additional information to the object writing. You can also provide
 * some logic to  treat a failed object write.
 *
 * The +listeners+ element can be used at the +step+ level or the +job+ level to define which listeners to run for each
 * batch processing event.
 *
 * include::myJob.xml[]
 *
 * @author Roberto Cortez
 */
@RunWith(Arquillian.class)
public class BatchListenersTest {
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
            .addPackage("org.javaee7.batch.batch.listeners")
            .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
            .addAsResource("META-INF/batch-jobs/myJob.xml");
        System.out.println(war.toString(true));
        return war;
    }

    /**
     * In the test, we're just going to invoke the batch execution and wait for completion. To validate the test
     * expected behaviour we need to query the +javax.batch.runtime.Metric+ object available in the step execution and
     * also verify if the listeners were executed correctly via a +CountDownLatch+ wait.
     *
     * The batch process itself will read and process 10 elements from numbers  1 to 10, but only write the odd
     * elements.
     *
     * * Each listener will decrement the total value of the +CountDownLatch+, until all the predicted events are
     * executed. The number of predicted events is 60:
     *
     * - +MyJobListener+ executes 2 times, 1 for +MyJobListener#beforeJob+ and 1 more for +MyJobListener#afterJob+.
     *
     * - +MyStepListener+ executes 2 times, 1 for +MyStepListener#beforeStep+ and 1 more for +MyStepListener#afterStep+.
     *
     * - +MyChunkListener+ executes 8 times, 4 for +MyChunkListener#beforeChunk+ and 4 more
     * for +MyChunkListener#afterChunk+. Chunk size is set to 3 and the total elements is 10, so 10/3 = 3 and 1 more
     * for the last element, means 4 for each chunk listener event.
     *
     * - +MyItemReader+ executes 22 times, 10 elements in total plus an empty read, so +MyItemReadListener#beforeRead+
     * executes 11 times and +MyItemReadListener#afterRead+ the other 11 times.
     *
     * - +MyItemProcessorListener+ executes 20 times, 10 elements read in total,
     * so +MyItemProcessorLister#beforeProcess+ executes 10 times
     * and +MyItemProcessorLister#afterProcess+ the other 10 times.
     *
     * - +MyItemWriterListener+ executed 6 times, 3 times for +MyItemWriterListener#beforeWrite+ and another 3 times
     * for +MyItemWriterListener#afterWrite+. This one is a bit more tricky, since not every element needs to be
     * written. Looking at +MyItemProcessor+, only even records are going to be written. We also need to take into
     * account the elements read per chunk, so: Chunk[1] read and process [1,2,3] and wrote [2,6], Chunk[2] read and
     * process [4,5,6] and wrote [10], Chunk[3] read and process [7,8,9] and wrote [14,18], Chunk[4] read and process
     * [10] and did not wrote anything, so only 3 writes for the full processing.
     *
     * - Total: 2 + 2 + 8 + 22 + 20 + 6 = 60
     *
     * @throws Exception an exception if the batch could not complete successfully.
     */
    @Test
    public void testBatchListeners() throws Exception {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Long executionId = jobOperator.start("myJob", new Properties());
        JobExecution jobExecution = jobOperator.getJobExecution(executionId);

        jobExecution = BatchTestHelper.keepTestAlive(jobExecution);

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
