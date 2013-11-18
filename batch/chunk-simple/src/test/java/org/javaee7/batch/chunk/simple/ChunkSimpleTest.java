package org.javaee7.batch.chunk.simple;

import org.javaee7.batch.test.BatchTestHelper;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.Metric;
import javax.batch.runtime.StepExecution;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Roberto Cortez
 */
@RunWith(Arquillian.class)
public class ChunkSimpleTest {
    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class)
                                   .addClass(BatchTestHelper.class)
                                   .addClass(MyInputRecord.class)
                                   .addClass(MyItemProcessor.class)
                                   .addClass(MyItemReader.class)
                                   .addClass(MyItemWriter.class)
                                   .addClass(MyOutputRecord.class)
                                   .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
                                   .addAsManifestResource("META-INF/batch-jobs/myJob.xml", "batch-jobs/myJob.xml");
        System.out.println(war.toString(true));
        return war;
    }

    @Test
    public void testChunkSimple() throws Exception {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Long executionId = jobOperator.start("myJob", new Properties());
        JobExecution jobExecution = jobOperator.getJobExecution(executionId);

        BatchTestHelper.keepTestAlive(jobExecution);

        List<StepExecution> stepExecutions = jobOperator.getStepExecutions(executionId);
        for (StepExecution stepExecution : stepExecutions) {
            if (stepExecution.getStepName().equals("myStep")) {
                Map<Metric.MetricType,Long> metricsMap = BatchTestHelper.getMetricsMap(stepExecution.getMetrics());
                Assert.assertEquals((long) metricsMap.get(Metric.MetricType.READ_COUNT), 10L);
                Assert.assertEquals((long) metricsMap.get(Metric.MetricType.WRITE_COUNT), 10L/2L);
                Assert.assertEquals((long) metricsMap.get(Metric.MetricType.COMMIT_COUNT), 10L/3 + 10%3);
            }
        }

        Assert.assertEquals(jobExecution.getBatchStatus(), BatchStatus.COMPLETED);
    }
}
