package org.javaee7.batch.batchlet.simple;

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
import java.util.Properties;

/**
 * @author Roberto Cortez
 */
@RunWith(Arquillian.class)
public class MyBatchletTest {

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class).
                addClass(MyBatchlet.class).
                addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml")).
                addAsResource("META-INF/batch-jobs/myJob.xml");
        System.out.println(war.toString(true));
        return war;
    }

    @Test
    public void testBatchletProcess() throws Exception {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Long executionId = jobOperator.start("myJob", new Properties());
        JobExecution jobExecution = jobOperator.getJobExecution(executionId);

        keepTestAlive(jobExecution);

        Assert.assertEquals(jobExecution.getBatchStatus(), BatchStatus.COMPLETED);
    }

    /**
     * We need to keep the test running because JobOperator runs the batch job in an asynchronous way, so the
     * JobExecution can be properly updated with the running job status.
     *
     * @param jobExecution the JobExecution of the job that is being runned on JobOperator
     * @throws InterruptedException
     */
    private void keepTestAlive(JobExecution jobExecution) throws InterruptedException {
        int maxTries = 0;
        while (!jobExecution.getBatchStatus().equals(BatchStatus.COMPLETED)) {
            if (maxTries < 10) {
                maxTries++;
                Thread.sleep(100);
            } else {
                break;
            }
        }
    }
}
