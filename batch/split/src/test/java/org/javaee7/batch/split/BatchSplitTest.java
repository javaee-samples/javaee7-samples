package org.javaee7.batch.split;

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
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.StepExecution;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * The Batch specification allows you to implement process workflow using a Job Specification Language (JSL). In this
 * sample, by using the +split+ element, it's possible to configure a job that runs parallel flows. A +split+ can only
 * contain +flow+ elements. These +flow+ elements can be used to implement separate executions to be processed by the
 * job.
 *
 * Three simple Batchlet's are configured in the file +myJob.xml+. +MyBatchlet1+ and +MyBatchlet2+ are setted up to
 * execute in parallel by using the +split+ and +flow+ elements. +MyBatchlet3+ is only going to execute after
 * +MyBatchlet1+ and +MyBatchlet2+ are both done with their job.
 *
 * include::myJob.xml[]
 *
 * @author Roberto Cortez
 */
@RunWith(Arquillian.class)
public class BatchSplitTest {
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
            .addPackage("org.javaee7.batch.split")
            .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
            .addAsResource("META-INF/batch-jobs/myJob.xml");
        System.out.println(war.toString(true));
        return war;
    }

    /**
     * In the test, we're just going to invoke the batch execution and wait for completion. To validate the test
     * expected behaviour we need to query +javax.batch.operations.JobOperator#getStepExecutions+.
     *
     * @throws Exception an exception if the batch could not complete successfully.
     */
    @Test
    public void testBatchSplit() throws Exception {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Long executionId = jobOperator.start("myJob", new Properties());
        JobExecution jobExecution = jobOperator.getJobExecution(executionId);

        jobExecution = BatchTestHelper.keepTestAlive(jobExecution);

        List<StepExecution> stepExecutions = jobOperator.getStepExecutions(executionId);
        List<String> executedSteps = new ArrayList<>();
        for (StepExecution stepExecution : stepExecutions) {
            executedSteps.add(stepExecution.getStepName());
        }

        // <1> Make sure all the steps were executed.
        assertEquals(3, stepExecutions.size());
        assertTrue(executedSteps.contains("step1"));
        assertTrue(executedSteps.contains("step2"));
        assertTrue(executedSteps.contains("step3"));

        // <2> Steps 'step1' and 'step2' can appear in any order, since they were executed in parallel.
        assertTrue(executedSteps.get(0).equals("step1") || executedSteps.get(0).equals("step2"));
        assertTrue(executedSteps.get(1).equals("step1") || executedSteps.get(1).equals("step2"));
        // <3> Step 'step3' is always the last to be executed.
        assertTrue(executedSteps.get(2).equals("step3"));

        // <4> Job should be completed.
        assertEquals(BatchStatus.COMPLETED, jobExecution.getBatchStatus());
    }
}
