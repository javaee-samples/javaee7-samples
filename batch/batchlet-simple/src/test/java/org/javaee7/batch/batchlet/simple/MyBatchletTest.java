package org.javaee7.batch.batchlet.simple;

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
import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * Batchlet is the simplest processing style available in the Batch specification. It's a task oriented step where the
 * task is invoked once, executes, and returns an exit status.
 *
 * A Batchlet need to implement +javax.batch.api.Batchlet+ interface or in alternative extend
 * +javax.batch.api.AbstractBatchlet+ that already provides empty implementations for all methods.
 *
 * include::MyBatchlet[]
 *
 * We are mostly interested in overriding +javax.batch.api.AbstractBatchlet#process+ to provide the behaviour that we
 * want to achieve with the Batchlet itself. Common cases include: copy files to process with a chunk oriented step,
 * startup and cleanup, or validations to your processing workflow.
 *
 * To run your Batchlet, just add it to the job xml file (+myJob.xml+).
 *
 * include::myJob.xml[]
 *
 * @author Roberto Cortez
 */
@RunWith(Arquillian.class)
public class MyBatchletTest {
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
            .addClass(MyBatchlet.class)
            .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
            .addAsResource("META-INF/batch-jobs/myJob.xml");
        System.out.println(war.toString(true));
        return war;
    }

    /**
     * In the test, we're just going to invoke the batch execution and wait for completion. To validate the test
     * expected behaviour we just need to check the Batch Status in the +javax.batch.runtime.JobExecution+ object. We
     * should get a +javax.batch.runtime.BatchStatus.COMPLETED+.
     *
     * @throws Exception an exception if the batch could not complete successfully.
     */
    @Test
    public void testBatchletProcess() throws Exception {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Long executionId = jobOperator.start("myJob", new Properties());
        JobExecution jobExecution = jobOperator.getJobExecution(executionId);

        jobExecution = BatchTestHelper.keepTestAlive(jobExecution);

        // <1> Job should be completed.
        assertEquals(jobExecution.getBatchStatus(), BatchStatus.COMPLETED);
    }
}
